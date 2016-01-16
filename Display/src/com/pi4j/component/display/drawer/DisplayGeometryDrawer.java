package com.pi4j.component.display.drawer;

import java.awt.Color;
import java.awt.Point;

import com.pi4j.component.display.Display;

@Deprecated
public class DisplayGeometryDrawer {
	private Display display;

	public DisplayGeometryDrawer(Display display) {
		this.display = display;
	}

	public void drawCircle(Point position, int radius, Color color) {
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
	
	public void fillCircle(Point position, int radius, Color color) {
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
