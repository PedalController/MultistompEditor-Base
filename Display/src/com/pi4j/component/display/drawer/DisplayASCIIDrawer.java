package com.pi4j.component.display.drawer;

import java.awt.Point;

public class DisplayASCIIDrawer {
	public void writeText(Point position, String text) {
		/*this.cursor.move(x, y);

		for (int i=0 ; i<str.length(); i++)
			LCDWrite((byte) str.charAt(i));*/
	}
	
	public void writeChar(char character) {
		/*if (y >= HEIGHT) return;
		if ((x+5) >= WIDTH) return;

		int i,j;

		for (i =0; i<5; i++) {
			int d = Jpcd8544Drawer.font[(c*5)+i];
			for (j = 0; j<8; j++)
				//if ((d & _BV(j)) == Math.pow(2, j))
					setPixel(x+i, y+j, Color.BLACK);
		}

		for (j = 0; j<8; j++)
			setPixel(x+5, y+j, textcolor == Color.BLACK ? Color.WHITE : Color.BLACK);

		updateBoundingBox(x, y, x+5, y + 8);*/
	}

	private void LCDWrite(int c) {
		/*
		int x = cursor.x;
		int y = cursor.y;

		if (c == '\n')	{
			y += textsize * Jpcd8544Drawer.FONT_HEIGHT;
			x = 0;

		} else if (c == '\r'){
			// skip em

		} else {
			LCDDrawChar(x, y, (char) c);
			x += textsize * (Jpcd8544Drawer.FONT_WIDTH + 1);

			if (x >= (WIDTH - Jpcd8544Drawer.FONT_WIDTH)) {
				x = 0;
				y += Jpcd8544Drawer.FONT_HEIGHT;
			}

			if (y >= HEIGHT)
				y = 0;
		}

		cursor.move(x, y);*/
	}
}
