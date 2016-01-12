package com.pi4j.component.display.test;

import java.awt.Point;

import com.pi4j.component.display.AWTDisplayComponent;
import com.pi4j.component.display.AWTDisplayComponent.Color;
import com.pi4j.component.display.Display;
import com.pi4j.component.display.DisplayGeometryDrawer;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		Display<Color> display = new AWTDisplayComponent(84, 48);
		DisplayGeometryDrawer<Color> drawer = new DisplayGeometryDrawer<Color>(display); 

		display.clear();
					

		System.out.println("Test: Display logo.\n");
		System.out.println("I'm Cleaned, huahuahu.\n");
		//LCDShowLogo();
		Thread.sleep(5000);
		display.clear();

		System.out.println("Test: Display single pixel.\n");
		display.setPixel(10, 10, Color.BLACK);
		display.refresh();

		Thread.sleep(5000);
		display.clear();

		System.out.println("Test: Draw many lines.\n");
		for (int i=0; i<84; i+=4)
			drawer.drawLine(new Point(0, 0), new Point(i, 47), Color.BLACK);

		for (int i=0; i<48; i+=4)
			drawer.drawLine(new Point(0, 0), new Point(83, i), Color.BLACK);

		display.refresh();
		Thread.sleep(5000);
		display.clear();

		System.out.println("Test: Draw rectangles.\n");
		for (int i=0; i<48; i+=2)
			drawer.drawRect(new Point(i, i), 96-i, 48-i, Color.BLACK);

		display.refresh();
		Thread.sleep(5000);
		display.clear();


		System.out.println("Test: Draw multiple rectangles.\n");
		for (int i=0; i<48; i++) {
			Color color = i%2 == 0 ? Color.BLACK : Color.WHITE;
			drawer.fillRect(new Point(i, i), 84-i, 48-i, color);
		}

		display.refresh();
		Thread.sleep(5000);
		display.clear();

		System.out.println("Test: Draw multiple circles.\n");
		for (int i=0; i<48; i+=2)
			drawer.drawCircle(new Point(41, 23), i, Color.BLACK);

		display.refresh();
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
}
