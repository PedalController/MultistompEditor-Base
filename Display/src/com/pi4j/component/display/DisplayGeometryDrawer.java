package com.pi4j.component.display;

import java.awt.Point;

import com.pi4j.component.display.Display.Color;

public class DisplayGeometryDrawer<C extends Color> {
	private Display<C> display;

	public DisplayGeometryDrawer(Display<C> display) {
		this.display = display;
	}

	/** Draw a line based in Wikipedia Bresenham's algorithm
	 */
	public void drawLine(Point start, Point end, C color) {	
		boolean steep = Math.abs(end.y - start.y) > Math.abs(end.x - start.x);

		if (steep) {
			start = swapPointValues(start);
			end = swapPointValues(end);
		}

		if (start.x > end.x){
			swapXOf(start, end);
			swapYOf(start, end);
		}

		int dx = end.x - start.x;
		int dy = Math.abs(end.y - start.y);

		int err = dx / 2;
		int ystep = start.y < end.y ? 1 : -1;

		for (; start.x<=end.x; start.x++) {
			if (steep)
				display.setPixel(start.y, start.x, color);
			else
				display.setPixel(start.x, start.y, color);

			err -= dy;
			if (err < 0){
				start.y += ystep;
				err += dx;
			}
		}
	}

	private Point swapPointValues(Point point) {
		return new Point(point.y, point.x);
	}

	private void swapXOf(Point p1, Point p2) {
		int x = p1.x;
		p1.x = p2.x;
		p2.x = x;
	}
	
	private void swapYOf(Point p1, Point p2) {
		int y = p1.y;
		p1.y = p2.y;
		p2.y = y;
	}

	public void drawRect(Point position, int width, int height, C color) {
		Point end = position.getLocation();
		end.translate(width, height);

		// stupidest version - just pixels - but fast with internal buffer!
		for (int i = position.x; i < end.x; i++) {
			display.setPixel(i, position.y, color);
			display.setPixel(i, end.y-1, color);
		}

		for (int i = position.y; i < end.y; i++) {
			display.setPixel(position.x, i, color);
			display.setPixel(end.x-1, i, color);
		}
	}
	
	public void fillRect(Point position, int width, int height, C color) {
		Point end = position.getLocation(); 
		end.translate(width, height);

		// stupidest version - just pixels - but fast with internal buffer!
		for (int i = position.x; i<end.x; i++)
			for (int j = position.y; j<end.y; j++)
				display.setPixel(i, j, color);
	}
	
	public void drawCircle(Point position, int radius, C color) {
		int f = 1 - radius;
		int ddF_x = 1;
		int ddF_y = -2 * radius;

		int x = 0;
		int y = radius;

		display.setPixel(position.x, position.y + radius, color);
		display.setPixel(position.x, position.y - radius, color);
		display.setPixel(position.x + radius, position.y, color);
		display.setPixel(position.x - radius, position.y, color);

		while (x < y) {
			if (f >= 0) {
				y--;
				ddF_y += 2;
				f += ddF_y;
			}

			x++;
			ddF_x += 2;
			f += ddF_x;

			display.setPixel(position.x + x, position.y + y, color);
			display.setPixel(position.x - x, position.y + y, color);
			display.setPixel(position.x + x, position.y - y, color);
			display.setPixel(position.x - x, position.y - y, color);

			display.setPixel(position.x + y, position.y + x, color);
			display.setPixel(position.x - y, position.y + x, color);
			display.setPixel(position.x + y, position.y - x, color);
			display.setPixel(position.x - y, position.y - x, color);
		}
	}
	
	public void fillCircle(Point position, int radius, C color) {
		int f = 1 - radius;
		int ddF_x = 1;
		int ddF_y = -2 * radius;
		int x = 0;
		int y = radius;
		int i;

		for (i = position.y-radius; i <= position.y+radius; i++)
			display.setPixel(position.x, i, color);

		while (x < y) {
			if (f >= 0) {
				y--;
				ddF_y += 2;
				f += ddF_y;
			}
			x++;
			ddF_x += 2;
			f += ddF_x;

			for (i=position.y-y; i<=position.y + y; i++) {
				display.setPixel(position.x+x, i, color);
				display.setPixel(position.x-x, i, color);
			}

			for (i=position.y-x; i<=position.y + x; i++) {
				display.setPixel(position.x+y, i, color);
				display.setPixel(position.x-y, i, color);
			}
		}
	}
}
