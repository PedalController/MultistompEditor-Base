package com.pi4j.component.display;

public interface Display<C extends Display.Color> {
	
	public interface Color {
		
	}

	void setPixel(int x, int y, C color);
	
	/**
	 * Repaint the display, updating changes caused by use of setPixel method  
	 */
	void refresh();
	
	/**
	 * Change the Display for initial stage
	 */
	void clear();

	int getWidth();
	int getHeight();
}
