package com.pi4j.component.display.impl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

import com.pi4j.component.display.drawer.buffer.PixelBuffer;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  AWTDisplayComponent.java  
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
 * Based in Erkki 22-Jul-2014
 * https://raw.githubusercontent.com/noxo/SnakePI4J/master/src/org/noxo/devices/AWTDisplay.java
 * 
 * <p>WARNING: This don't works in Raspberry Pi - Console mode. <br />
 * Use for visual interfaces tests :D 
 * </p>
 */
public class AWTDisplayComponent implements com.pi4j.component.display.Display {

    private Frame screen;
    private int width;
    private int height;
    
    private Queue<PixelBuffer> changesBuffer;
    private boolean debugMode;
    
    public AWTDisplayComponent(int width, int height, boolean debugMode) {
        this.width = width;
        this.height = height;

        this.debugMode = debugMode;

        this.changesBuffer = new LinkedList<>();

        screen = new java.awt.Frame();
        screen.setSize(width, height);
        screen.setUndecorated(true);
        screen.validate();
        screen.setVisible(true);

        Dimension resolution = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        screen.setLocation(resolution.width/2-width/2, resolution.height/2-height/2);        
    }

    @Override
    public void setPixel(int x, int y, Color color) {
        this.changesBuffer.add(new PixelBuffer(x, y, color));
    }

    @Override
    public void redraw() {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        while (!changesBuffer.isEmpty()) {
            PixelBuffer pixel = changesBuffer.remove();
            img.setRGB(pixel.x, pixel.y, pixel.getColor().getRGB());

            if (debugMode) {
                simulateGPIODelay();
    
                Graphics g = screen.getGraphics();
                g.drawImage(img, 0, 0, screen);
            }
        }

        if (!debugMode) {
            simulateGPIODelay();

            Graphics g = screen.getGraphics();
            g.drawImage(img, 0, 0, screen);
        }
    }

    private void simulateGPIODelay() {
        try {
            Thread.sleep(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clear() {
        changesBuffer.clear();

        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                changesBuffer.add(new PixelBuffer(x, y, Color.WHITE));
            }
        }
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }
}

