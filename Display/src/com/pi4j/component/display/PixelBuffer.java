package com.pi4j.component.display;

import java.awt.Color;

public class PixelBuffer {
	public final int x;
	public final int y;
	private Color color;
	
	public PixelBuffer(final int x, final int y, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public String toString() {
		return "Pixel(x="+x+", y="+y+")";
	}
}
