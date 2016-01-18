package com.pi4j.component.display.drawer;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;

import com.pi4j.component.display.Display;

/**
 * A Graphics implementation for any Display type
 */
public class DisplayGraphics extends Graphics {
	
	private Display display;

	private BufferedImage bufferedImage;
	private Graphics2D graphics;
	
	public enum ColorType { 
		BINARY(BufferedImage.TYPE_BYTE_BINARY),
		RGB(BufferedImage.TYPE_3BYTE_BGR),
		ARGB(BufferedImage.TYPE_4BYTE_ABGR),
		GRAY(BufferedImage.TYPE_BYTE_GRAY);
		
		private final int type;

		private ColorType(int type) {
			this.type = type;
		}
		
		public int getType() {
			return type;
		}
	}

	public DisplayGraphics(Display display, Color initialColor, ColorType type) {
		this.display = display;

		this.bufferedImage = new BufferedImage(display.getWidth(), display.getHeight(), type.getType());
		this.graphics = initGraphics(bufferedImage.createGraphics(), initialColor);
		
		this.setColor(initialColor);
	}

	private Graphics2D initGraphics(Graphics2D graphics, Color initialColor) {
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

		graphics.setBackground(Color.WHITE);
		graphics.fillRect(0, 0, this.display.getWidth(), this.display.getHeight());
		graphics.setColor(initialColor);

		return graphics;
	}

	public void clear() {
		this.graphics.clearRect(0, 0, display.getWidth(), display.getHeight());
	}

	@Override
	public void clearRect(int x, int y, int width, int height) {
		this.graphics.clearRect(x, y, width, height);
	}

	@Override
	public void clipRect(int x, int y, int width, int height) {
		this.graphics.clipRect(x, y, width, height);
	}

	@Override
	public void copyArea(int x, int y, int width, int height, int dx, int dy) {
		this.graphics.copyArea(x, y, width, height, dx, dy);
	}

	@Override
	public Graphics create() {
		return this.graphics.create();
	}

	@Override
	public void dispose() {
		//this.graphics.dispose();

		this.updateDisplay();
	}

	private void updateDisplay() {
		Color[][] pixels = ImageUtils.getPixelsOf(this.bufferedImage);
		this.drawDisplay(pixels, 0, 0);
		display.redraw();
	}

	private void drawDisplay(Color[][] pixels, int x, int y) {
		int height = pixels.length;
		int width = pixels[0].length;

		for (int yImage = 0; yImage < height; yImage++)
			for (int xImage = 0; xImage < width; xImage++)
				display.setPixel(x+xImage, y+yImage, pixels[yImage][xImage]);
	}

	@Override
	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		this.graphics.drawArc(x, y, width, height, startAngle, arcAngle);
	}

	@Override
	public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
		return graphics.drawImage(img, x, y, observer);
	}

	@Override
	public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
		return this.graphics.drawImage(img, x, y, bgcolor, observer);
	}

	@Override
	public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
		return this.graphics.drawImage(img, x, y, width, height, observer);
	}

	@Override
	public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
		return this.graphics.drawImage(img, x, y, width, height, bgcolor, observer);
	}

	@Override
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
		return this.graphics.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
	}

	@Override
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
		return this.graphics.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer);
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		this.graphics.drawLine(x1, y1, x2, y2);
	}

	@Override
	public void drawOval(int x, int y, int width, int height) {
		this.graphics.drawOval(x, y, width, height);
	}

	@Override
	public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		this.graphics.drawPolygon(xPoints, yPoints, nPoints);
	}

	@Override
	public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
		this.graphics.drawPolyline(xPoints, yPoints, nPoints);
	}

	@Override
	public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		this.graphics.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	@Override
	public void drawString(String str, int x, int y) {
		this.graphics.drawString(str, x, y);
	}

	@Override
	public void drawString(AttributedCharacterIterator iterator, int x, int y) {
		this.graphics.drawString(iterator, x, y);
	}

	@Override
	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		this.graphics.fillArc(x, y, width, height, startAngle, arcAngle);
	}

	@Override
	public void fillOval(int x, int y, int width, int height) {
		this.graphics.fillOval(x, y, width, height);
	}

	@Override
	public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		this.graphics.fillPolygon(xPoints, yPoints, nPoints);
	}

	@Override
	public void fillRect(int x, int y, int width, int height) {
		this.graphics.fillRect(x, y, width, height);
	}

	@Override
	public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		this.graphics.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	@Override
	public Shape getClip() {
		return this.graphics.getClip();
	}

	@Override
	public Rectangle getClipBounds() {
		return this.graphics.getClipBounds();
	}

	@Override
	public Color getColor() {
		return this.graphics.getColor();
	}

	@Override
	public Font getFont() {
		return this.graphics.getFont();
	}

	@Override
	public FontMetrics getFontMetrics(Font f) {
		return this.graphics.getFontMetrics(f);
	}

	@Override
	public void setClip(Shape clip) {
		this.graphics.setClip(clip);
	}

	@Override
	public void setClip(int x, int y, int width, int height) {
		this.graphics.setClip(x, y, width, height);
	}

	@Override
	public void setColor(Color c) {
		this.graphics.setColor(c);
	}

	@Override
	public void setFont(Font font) {
		this.graphics.setFont(font);
	}

	@Override
	public void setPaintMode() {
		this.graphics.setPaintMode();
	}

	@Override
	public void setXORMode(Color c1) {
		this.graphics.setXORMode(c1);
	}

	@Override
	public void translate(int x, int y) {
		this.graphics.translate(x, y);
	}
}
