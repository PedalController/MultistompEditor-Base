package com.pi4j.component.display.drawer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class TextUtils {
	/*
	public static Color[][] writeText(String string, int x, int y) {
		int width = 84;
		int height = 48;

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);

		Graphics2D graphics = image.createGraphics();

		graphics.setBackground(Color.WHITE);
		graphics.fillRect(0, 0, width, height);
		graphics.setColor(Color.BLACK);

		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

		Font font = new Font("Serif", Font.PLAIN, 15);
		graphics.setFont(font);

		graphics.drawString(string, x, y);
		graphics.dispose();
		
		return ImageUtils.getPixelsOf(image);
	}
	*/
}
