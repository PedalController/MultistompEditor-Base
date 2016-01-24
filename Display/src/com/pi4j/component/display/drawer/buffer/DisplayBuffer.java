package com.pi4j.component.display.drawer.buffer;

import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  DisplayBuffer.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 * 
 * @author SrMouraSilva
 */

/**
 * <p>DisplayBuffer its a good auto dettecter changes. </p>
 * 
 * <p>
 * Change the required pixels and - for update the
 * real component display - calls getChanges() method.</p>
 * 
 * <p>
 * This will only return the pixel that has actually changed, avoiding unnecessary updates.
 * </p>
 * 
 * <p>
 * This buffer is a good choice for displays that 
 * is necessary send pixels colors one by one (usually a pixel
 * is a byte in this case). <br />
 * If a byte represents more than one color, this probably don't work.
 * You need create a special implementation in these cases. 
 * Find class PCB8544DisplayDataRam for inspiration!
 * </p>
 */
public class DisplayBuffer implements Iterable<PixelBuffer> {
    private Queue<PixelBuffer> changes = new LinkedList<>();

    private PixelBuffer[][] buffer;

    private final int width;
    private final int height;
    private final Color defaultColor;

    /**
     * @param width Total columns
     * @param height Total rows
     * @param defaultColor Initializes the pixels with this color
     *                     (in the initial state, is assumed that all the pixels are in the currentColor)
     */
    public DisplayBuffer(int width, int height, Color defaultColor) {
        this.width = width;
        this.height = height;
        this.buffer = new PixelBuffer[width][height];
        this.defaultColor = defaultColor;
    }

    /**
     * Set a specific pixel for a color 
     * 
     * @param x Row position. 0 is first, top to down direction
     * @param y Column position. 0 is first, left to right direction
     * @param color
     */
    public void setPixel(int x, int y, Color color) {
        Optional<PixelBuffer> pixel = getPixel(x, y);
        
        if (!pixel.isPresent()) {
            return; // or throws?

        } else if (pixel.get().getColor().equals(color)) {
            return;
        }

        pixel.get().setColor(color);
        changes.add(pixel.get());
    }

    private Optional<PixelBuffer> getPixel(int x, int y) {
        if (x < 0 || x > width-1
         || y < 0 || y > height-1) {
            return Optional.empty();
        }

        PixelBuffer pixel = buffer[x][y];
        if (pixel == null) {
            pixel  = new PixelBuffer(x, y, defaultColor);
            buffer[x][y] = pixel;
        }

        return Optional.of(pixel);
    }

    /**
     * <p>
     *   <strong>CAUTION</strong><br />
     *   For any iterator.hasNext(), the element returned
     *   should be removed in the changes queue
     * </p>
     * 
     * @return All changes detected
     */
    public Iterator<PixelBuffer> getChanges() {
        return iterator();
    }

    @Override
    public Iterator<PixelBuffer> iterator() {
        return new DisplayBufferIterator(changes);
    }
    
    public class DisplayBufferIterator implements Iterator<PixelBuffer> {
        private Queue<PixelBuffer> changes;
        
        private PixelBuffer next = null;

        public DisplayBufferIterator(Queue<PixelBuffer> changes) {
            this.changes = changes;
        }

        @Override
        public boolean hasNext() {
            next = findNext();
            return next != null;
        }

        private PixelBuffer findNext() {
            PixelBuffer pixelBuffer;
            do {
                pixelBuffer = changes.poll();
            } while (pixelBuffer != null && !pixelBuffer.hasRealChange());
            
            return pixelBuffer;
        }

        @Override
        public PixelBuffer next() {
            PixelBuffer pixelBuffer = next;
            pixelBuffer.updateLastChangeColor();

            return pixelBuffer;
        }
    }
}
