package com.pi4j.component.display.drawer;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;

import com.pi4j.component.display.Display;

/**
 * A Graphics implementation for any Display type
 */
public class DisplayGraphics extends Graphics {
	
	private Display display;
	private Color currentColor;
	private Color initialColor;

	public DisplayGraphics(Display display, Color initialColor) {
		this.display = display;
		this.initialColor = initialColor;
	}

	@Override
	public void clearRect(int x, int y, int width, int height) {
		Color current = this.currentColor;

		this.setColor(initialColor);
		this.fillRect(x, y, width, height);
		this.setColor(current);
	}

	@Override
	public void clipRect(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
	}

	@Override
	public void copyArea(int x, int y, int width, int height, int dx, int dy) {
		// TODO Auto-generated method stub
	}

	@Override
	public Graphics create() {
		DisplayGraphics graphics = new DisplayGraphics(display, initialColor);
		graphics.setColor(currentColor);

		return graphics;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public void drawArc(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean drawImage(Image image, int x, int y, ImageObserver observer) {
		Color[][] pixels = ImageUtils.getPixelsOf(image);

		int height = pixels.length;
		int width = pixels[0].length;

		for (int yImage = 0; yImage < height; yImage++)
			for (int xImage = 0; xImage < width; xImage++)
				display.setPixel(x+xImage, y+yImage, pixels[yImage][xImage]);

		return true;
	}

	@Override
	public boolean drawImage(Image arg0, int arg1, int arg2, Color arg3, ImageObserver arg4) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean drawImage(Image arg0, int arg1, int arg2, int arg3, int arg4, ImageObserver arg5) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean drawImage(Image image, int arg1, int arg2, int arg3, int arg4, Color arg5, ImageObserver arg6) {
		// TODO Auto-generated method stub
		return false;
	}
	

	@Override
	public boolean drawImage(Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, ImageObserver arg9) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean drawImage(Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, Color arg9, ImageObserver arg10) {
		// TODO Auto-generated method stub
		return false;
	}

	/** Draw a line based in Wikipedia Bresenham's algorithm
	 */
	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		Point start = new Point(x1, y1);
		Point end   = new Point(x2, y2);

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
				display.setPixel(start.y, start.x, currentColor);
			else
				display.setPixel(start.x, start.y, currentColor);

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

	@Override
	public void drawOval(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawPolygon(int[] arg0, int[] arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawPolyline(int[] arg0, int[] arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawRoundRect(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawString(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawString(AttributedCharacterIterator arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	public void fillArc(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
		// TODO Auto-generated method stub
	}

	@Override
	public void fillOval(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void fillPolygon(int[] arg0, int[] arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	public void fillRect(int x, int y, int width, int height) {
		Point position = new Point(x, y);

		Point end = position.getLocation(); 
		end.translate(width, height);

		for (int i = position.x; i<end.x; i++)
			for (int j = position.y; j<end.y; j++)
				display.setPixel(i, j, currentColor);
	}

	@Override
	public void fillRoundRect(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
		// TODO Auto-generated method stub
	}

	@Override
	public Shape getClip() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getClipBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getColor() {
		return this.currentColor;
	}

	@Override
	public Font getFont() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FontMetrics getFontMetrics(Font arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setClip(Shape arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setClip(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setColor(Color color) {
		this.currentColor = color;
	}

	@Override
	public void setFont(Font arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setPaintMode() {
		// TODO Auto-generated method stub
	}

	@Override
	public void setXORMode(Color arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void translate(int arg0, int arg1) {
		// TODO Auto-generated method stub
	}
}
