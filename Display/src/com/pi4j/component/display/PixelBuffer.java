package com.pi4j.component.display;

import com.pi4j.component.display.Display.Color;

public class PixelBuffer<C extends Color> {
	public final int x;
	public final int y;
	private C color;
	
	public PixelBuffer(final int x, final int y, C color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}

	public C getColor() {
		return color;
	}

	public void setColor(C color) {
		this.color = color;
	}
	
	@Override
	public String toString() {
		return "Pixel(x="+x+", y="+y+")";
	}
}
