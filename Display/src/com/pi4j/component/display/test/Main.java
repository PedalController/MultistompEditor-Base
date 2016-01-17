package com.pi4j.component.display.test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.pi4j.component.display.Display;
import com.pi4j.component.display.WhiteBlackDisplay;
import com.pi4j.component.display.drawer.DisplayGraphics;
import com.pi4j.component.display.impl.AWTDisplayComponent;
import com.pi4j.component.display.impl.PCD8544DisplayComponent;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Inicializando");
		Display display = getDisplayComponent();

		Graphics graphics = new DisplayGraphics(display, Color.WHITE);
		graphics.setColor(WhiteBlackDisplay.BLACK); 

		System.out.println("Dando um Clear");
		display.clear();

		Thread.sleep(5000);
		
		System.out.println("Test: Tentando imprimir algo\n");
		graphics.drawString("Macarronada", 0, 20);
		display.redraw();
		Thread.sleep(5000);
		display.clear();

		System.out.println("Test: Display single pixel.\n");
		display.setPixel(10, 10, Color.BLACK);
		display.redraw();

		Thread.sleep(5000);
		display.clear();


		System.out.println("Test: Draw image.\n");
		String baseName = System.getProperty("user.dir") + File.separator + "lib" + File.separator;
		//String imageName = baseName + "Aco.Sim.bmp";
		String imageName = baseName + "pi4j-header-small3.png";

		try {
			Image image = ImageIO.read(new File(imageName));

			graphics.drawImage(image, 0, 0, null);
			display.redraw();
			Thread.sleep(5000);
			display.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Monochomatic
		try {
			Image image = ImageIO.read(new File(imageName));
			BufferedImage bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_BYTE_BINARY);

			Graphics2D bGr = bimage.createGraphics();
			bGr.drawImage(image, 0, 0, null);
			bGr.dispose();

			graphics.drawImage(bimage, 0, 0, null);
			display.redraw();
			Thread.sleep(5000);
			display.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}


		System.out.println("Test: Draw many lines.\n");
		for (int i=0; i<84; i+=4) {
			graphics.drawLine(0, 0, i, 47);
			display.redraw();
		}

		for (int i=0; i<48; i+=4) {
			graphics.drawLine(0, 0, 83, i);
			display.redraw();
		}

		display.redraw();
		Thread.sleep(5000);
		display.clear();

		System.out.println("Test: Draw rectangles.\n");
		for (int i=0; i<48; i+=2) {
			graphics.drawRect(i, i, 83-i, 47-i);
			display.redraw();
		}

		display.redraw();
		Thread.sleep(5000);
		display.clear();


		System.out.println("Test: Draw multiple rectangles.\n");
		for (int i=0; i<48; i++) {
			Color color = i%2 == 0 ? Color.BLACK : Color.WHITE;
			graphics.setColor(color);
			graphics.fillRect(i, i, 83-i, 47-i);
			display.redraw();
		}

		display.redraw();
		Thread.sleep(5000);
		display.clear();

		System.out.println("Test: Draw multiple circles.\n");
		for (int i=0; i<48; i+=2) {
			graphics.drawOval(41, 23, i, i);
			display.redraw();
		}

		display.redraw();
		Thread.sleep(5000);
		display.clear();
		
		
		/*
		System.out.println("Test: Draw the first ~120 chars.\n");
		for (int i=0; i < 64; i++) {
			drawer.LCDDrawChar((i % 14) * 6, (i/14) * 8, (char)i);
		}		
		display.refresh();
		Thread.sleep(5000);
		for (int i=0; i < 64; i++)
			drawer.LCDDrawChar((i % 14) * 6, (i/14) * 8, (char)(i + 64));

		display.refresh();
		Thread.sleep(5000);
		display.clear();
		*/
	}

	private static Display getDisplayComponent() {
		return new AWTDisplayComponent(500, 400);
		
		/** /
		GpioController gpio = GpioFactory.getInstance();

		GpioPinDigitalOutput RST = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_15, PinState.LOW);
		GpioPinDigitalOutput SCE = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, PinState.LOW);
		GpioPinDigitalOutput DC  = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW);
		GpioPinDigitalOutput DIN = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW);
		GpioPinDigitalOutput CLK = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW);

		return new PCD8544DisplayComponent(
			DIN,
			CLK,
			DC,
			RST,
			SCE,
			(byte) 60/*0xB0* /,
			false
		);
		/**/
	}
}
