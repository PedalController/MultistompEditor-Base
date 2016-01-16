package com.pi4j.component.display.drawer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ImageUtils {

	public static Color[][] getPixelsOf(Image image) {
		BufferedImage bufferedImage = convertToBufferedImage(image);
		byte[] pixels = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();

		final boolean hasAlphaChannel = bufferedImage.getAlphaRaster() != null;

		if (hasAlphaChannel)
			return getPixelsAlpha(bufferedImage, pixels);
		else if (bufferedImage.getType() == BufferedImage.TYPE_BYTE_BINARY)
			return getByteBinaryPixels(bufferedImage, pixels);
		else
			return getPixels(bufferedImage, pixels);
	}

	private static Color[][] getPixelsAlpha(BufferedImage bufferedImage, byte[] pixels) {
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

	private static Color[][] getByteBinaryPixels(BufferedImage bufferedImage, byte[] pixels) {
		final int width = bufferedImage.getWidth();
		final int height = bufferedImage.getHeight();

		Color[][] result = new Color[height][width];
		System.out.println(pixels.length);
		
		int x = 0;
		int y = 0;
		for (byte pixel : pixels) {
			for (int i = 7; i >= 0; i--) {
				byte color = (byte) ((pixel & 1 << i) >> i);

				result[y][x] = color == 0 ? Color.BLACK : Color.WHITE;
				x++;

				if (x == width) {
					x = 0;
					y++;
				}
				if (y == height)
					return result;
			}
		}

		return result;
	}

	private static Color[][] getPixels(BufferedImage bufferedImage, byte[] pixels) {
		final int width = bufferedImage.getWidth();
		final int height = bufferedImage.getHeight();

		Color[][] result = new Color[height][width];

		final int pixelLength = 3;
		for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {

			int blue  = pixels[pixel] & 0xff;
			int green = (pixels[pixel + 1] & 0xff);
			int red   = (pixels[pixel + 2] & 0xff);

			try {
				result[row][col] = new Color(red, green, blue);
			} catch (Exception e) {
				System.out.println(Integer.toHexString(blue));
				System.out.println(Integer.toHexString(red));
				System.out.println(Integer.toHexString(green));
				e.printStackTrace();
			}
			

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
