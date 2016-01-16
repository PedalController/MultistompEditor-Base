package com.pi4j.component.display.test;

import java.awt.Point;

import com.pi4j.component.display.drawer.DisplayGeometryDrawer;
import com.pi4j.component.display.impl.AWTDisplayComponent;
import com.pi4j.component.display.impl.PCD8544DisplayComponent;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.component.display.Display;
import com.pi4j.component.display.WhiteBlackDisplay;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Inicializando");
		Display<WhiteBlackDisplay.Color> display = getDisplayComponent();

		DisplayGeometryDrawer<WhiteBlackDisplay.Color> drawer = new DisplayGeometryDrawer<WhiteBlackDisplay.Color>(display); 

		System.out.println("Dando um Clear");
		display.clear();
					

		//System.out.println("Test: Display logo.\n");
		//System.out.println("I'm Cleaned, huahuahu.\n");
		//Thread.sleep(5000);
		//System.out.println("Dando um Clear");
		//display.clear();

		System.out.println("Test: Display single pixel.\n");
		display.setPixel(10, 10, WhiteBlackDisplay.Color.BLACK);
		display.redraw();

		Thread.sleep(3000);
		display.clear();

		System.out.println("Test: Draw many lines.\n");
		for (int i=0; i<84; i+=4)
			drawer.drawLine(new Point(0, 0), new Point(i, 47), WhiteBlackDisplay.Color.BLACK);

		for (int i=0; i<48; i+=4)
			drawer.drawLine(new Point(0, 0), new Point(83, i), WhiteBlackDisplay.Color.BLACK);

		display.redraw();
		Thread.sleep(3000);
		display.clear();

		System.out.println("Test: Draw rectangles.\n");
		for (int i=0; i<48; i+=2)
			drawer.drawRect(new Point(i, i), 83-i, 47-i, WhiteBlackDisplay.Color.BLACK);

		display.redraw();
		Thread.sleep(3000);
		display.clear();


		System.out.println("Test: Draw multiple rectangles.\n");
		for (int i=0; i<48; i++) {
			WhiteBlackDisplay.Color color = i%2 == 0 ? WhiteBlackDisplay.Color.BLACK : WhiteBlackDisplay.Color.WHITE;
			drawer.fillRect(new Point(i, i), 83-i, 47-i, color);
		}

		display.redraw();
		Thread.sleep(3000);
		display.clear();

		System.out.println("Test: Draw multiple circles.\n");
		for (int i=0; i<48; i+=2)
			drawer.drawCircle(new Point(41, 23), i, WhiteBlackDisplay.Color.BLACK);

		display.redraw();
		Thread.sleep(3000);
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

	private static Display<WhiteBlackDisplay.Color> getDisplayComponent() {
		//return new AWTDisplayComponent(84, 48);
		
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
			(byte) 80/*0xB0*/,
			false
		);
	}
}
