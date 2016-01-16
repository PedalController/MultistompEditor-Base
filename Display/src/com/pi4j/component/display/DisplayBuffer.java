package com.pi4j.component.display;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class DisplayBuffer {
	private Queue<PixelBuffer> changes = new LinkedList<>();

	private PixelBuffer[][] buffer;

	private final int width;
	private final int height;
	private final Color defaultColor;

	public DisplayBuffer(int width, int height, Color defaultColor) {
		this.width = width;
		this.height = height;
		this.buffer = new PixelBuffer[width][height];
		this.defaultColor = defaultColor;
	}

	public void setPixel(int x, int y, Color color) {
		Optional<PixelBuffer> pixel = getPixel(x, y);
		
		if (!pixel.isPresent())
			return; // or throws?

		if (pixel.get().getColor().equals(color))
			return;

		pixel.get().setColor(color);
		changes.add(pixel.get());
	}

	private Optional<PixelBuffer> getPixel(int x, int y) {
		if (x < 0 || x > width-1
		 || y < 0 || y > height-1)
			return Optional.empty();

		PixelBuffer pixel = buffer[x][y];
		if (pixel == null) {
			pixel  = new PixelBuffer(x, y, defaultColor);
			buffer[x][y] = pixel;
		}

		return Optional.of(pixel);
	}
	
	public Queue<PixelBuffer> getChanges() {
		return changes;
	}
}
