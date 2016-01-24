package com.pi4j.component.display.drawer.buffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Point;
import java.util.Iterator;

import org.junit.Test;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  DisplayBufferTest.java  
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
 */

public class DisplayBufferTest {

    /**
     * For a N where 
     *  - N is natural number 
     *  - 0 <= N < infinite 
     *  - N = all the display pixels (width*height)
     * 
     * if change the M where
     *  - M is natural number
     *  - 0 <= M <= N
     *  - M is the total pixels changed
     * 
     * the changes registred are only the M pixels.
     */
    @Test
    public void onlyUpdateThePixelsChangedTest() {
        final int SIZE = 10;
        DisplayBuffer buffer = new DisplayBuffer(SIZE, SIZE, Color.WHITE);
        
        Point pixel = new Point(5, 5); 
        buffer.setPixel(pixel.x, pixel.y, Color.BLACK);
 
        for (PixelBuffer pixelBuffer : buffer) {
            assertEquals(pixel.x, pixelBuffer.x);
            assertEquals(pixel.y, pixelBuffer.y);
            assertEquals(Color.BLACK, pixelBuffer.getColor());
        }
    }

    /**
     * For a N where 
     *  - N is natural number 
     *  - 0 <= N < infinite 
     *  - N = all the display pixels (width*height)
     * 
     * if change the M where
     *  - M is natural number
     *  - 0 <= M <= N
     *  - M is the total pixels changed to same color
     * 
     * the changes registred are 0
     */
    @Test
    public void setAPixelToTheCurrentColorTest() {
        final int SIZE = 10;
        DisplayBuffer buffer = new DisplayBuffer(SIZE, SIZE, Color.WHITE);
        
        Point pixel = new Point(5, 5); 
        buffer.setPixel(pixel.x, pixel.y, Color.WHITE);

        Iterator<PixelBuffer> changes = buffer.getChanges();
        assertFalse(changes.hasNext());
    }
    
    /**
     * For a Display with 
     *  - size N width  (0 ... N-1)
     *  - size M height (0 ... M-1) 
     * 
     * if change a out of range pixel position
     *  - Position (x, y) where
     *    - (x >= N || x < 0) or (y >= M || y < 0) 
     * 
     * Nothing happens!
     */
    @Test
    public void setAPixelOutOfRangeTest() {
        final int SIZE = 10;
        DisplayBuffer buffer = new DisplayBuffer(SIZE, SIZE, Color.WHITE);
        
        buffer.setPixel(SIZE+10, 0, Color.RED);
        buffer.setPixel(0, SIZE+10, Color.RED);
        
        buffer.setPixel(SIZE, 0, Color.PINK);
        buffer.setPixel(0, SIZE, Color.GREEN);
        
        buffer.setPixel(-1, 0, Color.CYAN);
        buffer.setPixel(0, -1, Color.YELLOW);
    
        Iterator<PixelBuffer> changes = buffer.getChanges();
        assertFalse(changes.hasNext());
    }
    
    /**
     * If set a pixel to another color (x) and
     * set to another color (y) and (x == y or x != y),
     * only a change is registred, not two
     */
    @Test
    public void setAPixelAndSetAgainTest() {
        final int SIZE = 10;
        DisplayBuffer buffer = new DisplayBuffer(SIZE, SIZE, Color.WHITE);
        
        Point pixel = new Point(0, 0);
        buffer.setPixel(pixel.x, pixel.y, Color.RED);
        buffer.setPixel(pixel.x, pixel.y, Color.BLUE);

        int totalChanges = 0;
        
        for (PixelBuffer pixelBuffer : buffer) {
            assertEquals(pixel.x, pixelBuffer.x);
            assertEquals(pixel.y, pixelBuffer.y);
            assertEquals(Color.BLUE, pixelBuffer.getColor());
            totalChanges ++;
        }

        assertEquals(1, totalChanges);
    }

    /**
     * If set a pixel to another color and
     * set it for the inicial color the change isn't registred :/
     */
    @Test
    public void setAPixelAndResetItTest() {
        final int SIZE = 10;
        DisplayBuffer buffer = new DisplayBuffer(SIZE, SIZE, Color.WHITE);
        
        buffer.setPixel(0, 0, Color.RED);
        buffer.setPixel(0, 0, Color.WHITE);

        assertFalse(buffer.getChanges().hasNext());
    }
    
    /**
     * After obtaining and iterate the changes, the list of changes should be empty
     */
    @Test
    public void resetTheChangesListAfterObtainingAndIterateThemTest() {
        final int SIZE = 10;
        DisplayBuffer buffer = new DisplayBuffer(SIZE, SIZE, Color.WHITE);
        
        buffer.setPixel(0, 0, Color.RED);

        // Yes, exists!
        assertTrue(buffer.getChanges().hasNext());

        // To iterate for remove the elements in changes queue
        Iterator<PixelBuffer> iterator = buffer.getChanges();
        while (iterator.hasNext()) {
            iterator.next();
        }

        // getChanges reset the changes
        // should be 0 (hasn't next)
        assertFalse(buffer.getChanges().hasNext());
    }
}