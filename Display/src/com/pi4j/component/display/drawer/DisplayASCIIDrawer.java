package com.pi4j.component.display.drawer;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.text.AttributedCharacterIterator;
import java.util.Hashtable;

@Deprecated
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
	

	public static void main(String[] args) {
		AffineTransform af = new AffineTransform(5, 5, 10, 10, 20, 20);
		FontRenderContext context = new FontRenderContext(af, false, false);
		Font font = new Font("Monospaced", Font.PLAIN, 32);
		GlyphVector glyphVector = font.createGlyphVector(context, "b");
		
		//System.out.println((char) glyphVector.getGlyphCode(0));
		
		Polygon p = new Polygon();
	    for (int i = 0; i < 5; i++)
	    	p.addPoint(
    			(int) (100 + 50 * Math.cos(i * 2 * Math.PI / 5)),
    			(int) (100 + 50 * Math.sin(i * 2 * Math.PI / 5))
			);

	    Rectangle2D r2 = new Rectangle2D.Float(25,25,150,150);
	    PathIterator pathIterator = r2.getPathIterator(af);
	    
	    int type;
	    
		//Shape shape = glyphVector.getOutline();
		//shape.
		//PathIterator pathIterator = shape.getPathIterator(af);
	    //Path a;
	    float[] coords = new float[6];
		while (!pathIterator.isDone()) {
			System.out.println((byte) pathIterator.getWindingRule());
			type = pathIterator.currentSegment(coords);
			System.out.println(type);
			System.out.println(coords);
			pathIterator.next();
		}
	}
}
