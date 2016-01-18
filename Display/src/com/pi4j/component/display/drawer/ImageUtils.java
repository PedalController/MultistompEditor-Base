package com.pi4j.component.display.drawer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ImageUtils {

	public static Color[][] getPixelsOf(Image image) {
		BufferedImage bufferedImage = convertToBufferedImage(image);

		final boolean hasAlphaChannel = bufferedImage.getAlphaRaster() != null;

		if (hasAlphaChannel)
			return getPixelsARGB(bufferedImage);
		else if (bufferedImage.getType() == BufferedImage.TYPE_3BYTE_BGR)
			return getPixelsRGB(bufferedImage);
		else
			return getSlowBinaryPixels(bufferedImage);
	}

	private static Color[][] getPixelsARGB(BufferedImage bufferedImage) {
		byte[] pixels = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();

		final int width = bufferedImage.getWidth();
		final int height = bufferedImage.getHeight();

		Color[][] result = new Color[height][width];

		final int pixelLength = 4;
		for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {

			int argb = 0;
			argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
			argb += ((int) pixels[pixel + 1] & 0xff); // blue
			argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
			argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red

			result[row][col] = new Color(argb, true);
			col++;

			if (col == width) {
				col = 0;
				row++;
			}
		}
		
		return result;
	}

	private static Color[][] getSlowBinaryPixels(BufferedImage bufferedImage) {
		final int width = bufferedImage.getWidth();
		final int height = bufferedImage.getHeight();

		Color[][] result = new Color[height][width];

		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				result[y][x] = new Color(bufferedImage.getRGB(x, y));

		return result;
	}

	private static Color[][] getPixelsRGB(BufferedImage bufferedImage) {
		byte[] pixels = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();

		final int width = bufferedImage.getWidth();
		final int height = bufferedImage.getHeight();

		Color[][] result = new Color[height][width];

		final int pixelLength = 3;
		for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {

			int blue  = pixels[pixel] & 0xff;
			int green = (pixels[pixel + 1] & 0xff);
			int red   = (pixels[pixel + 2] & 0xff);

			result[row][col] = new Color(red, green, blue);

			col++;
			if (col == width) {
				col = 0;
				row++;
			}
		}
		
		return result;
	}

	private static BufferedImage convertToBufferedImage(Image img) {
		if (img instanceof BufferedImage)
			return (BufferedImage) img;

		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		return bimage;
	}
}
