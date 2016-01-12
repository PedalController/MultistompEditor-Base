package com.pi4j.component.display;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

import com.pi4j.component.display.Display.Color;

public class DisplayBuffer<C extends Color> {
	private Queue<PixelBuffer<C>> changes = new LinkedList<>();

	private PixelBuffer<C>[][] buffer;

	private final int width;
	private final int height;
	private final C defaultColor;

	public DisplayBuffer(int width, int height, C defaultColor) {
		this.width = width;
		this.height = height;
		this.buffer = new PixelBuffer[width][height];
		this.defaultColor = defaultColor;
	}

	public void setPixel(int x, int y, C color) {
		Optional<PixelBuffer<C>> pixel = getPixel(x, y);
		
		if (!pixel.isPresent())
			return; // or throws?

		if (pixel.get().getColor().equals(color))
			return;

		pixel.get().setColor(color);
		changes.add(pixel.get());
	}

	private Optional<PixelBuffer<C>> getPixel(int x, int y) {
		if (x < 0 || x > width-1
		 || y < 0 || y > height-1)
			return Optional.empty();

		PixelBuffer<C> pixel = buffer[x][y];
		if (pixel == null) {
			pixel  = new PixelBuffer<C>(x, y, defaultColor);
			buffer[x][y] = pixel;
		}

		return Optional.of(pixel);
	}
	
	public Queue<PixelBuffer<C>> getChanges() {
		return changes;
	}
}
