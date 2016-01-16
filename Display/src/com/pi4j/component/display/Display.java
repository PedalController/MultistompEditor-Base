package com.pi4j.component.display;

import java.awt.Color;

public interface Display {

	void setPixel(int x, int y, Color color);
	
	/**
	 * Repaint the display, updating changes caused by use of setPixel method  
	 */
	void redraw();
	
	/**
	 * Change the Display for initial stage
	 */
	void clear();

	int getWidth();
	int getHeight();
}
