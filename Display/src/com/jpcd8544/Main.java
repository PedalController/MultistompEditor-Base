package com.jpcd8544;

import com.pi4j.component.display.WhiteBlackDisplay;
import com.pi4j.component.display.WhiteBlackDisplay.Color;

public class Main {

	public static void main(String args[]){
		JPCD8544DisplayComponent lcd = new JPCD8544DisplayComponent();
		lcd.LCDInit(50);

		showMe(lcd);
	}

	/**
	 * Do some tests with the library, using the default GPIO Pins
	 * But you have to initialize the LCD first
	 * 
	 */
	public static void showMe(WhiteBlackDisplay display) {
		try {
			// print infos
			System.out.println("Raspberry Pi PCD8544 test in Java\n");
			System.out.println("========================================\n");
			System.out.println("CLK on Port "+_sclk.getPin().getName()+"\n");
			System.out.println("DIN on Port "+_din.getPin().getName()+"\n");
			System.out.println("DC on Port "+_dc.getPin().getName()+"\n");
			System.out.println("CS on Port "+_cs.getPin().getName()+"\n");
			System.out.println("RST on Port "+_rst.getPin().getName()+"\n");	
			System.out.println("========================================\n");

			// init and clear lcd
			display.clear();

			// turn all the pixels on (a handy test)
			System.out.println("Test: All pixels on.\n");
			LCDCommand(PCD8544_DISPLAYCONTROL | PCD8544_DISPLAY_ALLON);
			Thread.sleep(1000);
			
			// back to normal
			System.out.println("Test: All pixels off.\n");
			LCDCommand(PCD8544_DISPLAYCONTROL | PCD8544_DISPLAY_NORMAL);
			display.clear();
			
			// display logo
			System.out.println("Test: Display logo.\n");
			System.out.println("I'm Cleaned, huahuahu.\n");
			//LCDShowLogo();
			Thread.sleep(2000);
			display.clear();

			// draw a single pixel
			System.out.println("Test: Display single pixel.\n");
			((JPCD8544DisplayComponent) display).setPixel(10, 10, Color.BLACK);
			((JPCD8544DisplayComponent) display).LCDDisplay();
			Thread.sleep(2000);
			display.clear();
			
			// draw many lines
			System.out.println("Test: Draw many lines.\n");
			for (int i=0; i<84; i+=4)
				((JPCD8544DisplayComponent) display).drawLine(0, 0, i, 47, Color.BLACK);

			for (int i=0; i<48; i+=4)
				((JPCD8544DisplayComponent) display).drawLine(0, 0, 83, i, Color.BLACK);

			((JPCD8544DisplayComponent) display).LCDDisplay();
			Thread.sleep(2000);
			display.clear();

			// draw rectangles
			System.out.println("Test: Draw rectangles.\n");
			for (int i=0; i<48; i+=2)
				((JPCD8544DisplayComponent) display).drawRect(i, i, 96-i, 48-i, Color.BLACK);

			((JPCD8544DisplayComponent) display).LCDDisplay();
			Thread.sleep(2000);
			display.clear();
			
			// draw multiple rectangles
			System.out.println("Test: Draw multiple rectangles.\n");
			for (int i=0; i<48; i++) {
				Color color = i%2 == 0 ? Color.BLACK : Color.WHITE;
				((JPCD8544DisplayComponent) display).fillRect(i, i, 84-i, 48-i, color);
			}

			((JPCD8544DisplayComponent) display).LCDDisplay();
			Thread.sleep(2000);
			display.clear();

			// draw mulitple circles
			System.out.println("Test: Draw multiple circles.\n");
			for (int i=0; i<48; i+=2) {
				Color color = i%2 == 0 ? Color.BLACK : Color.WHITE;
				((JPCD8544DisplayComponent) display).drawCircle(41, 23, i, color);
			}

			((JPCD8544DisplayComponent) display).LCDDisplay();
			Thread.sleep(2000);
			display.clear();
			
			// draw the first ~120 characters in the font
			System.out.println("Test: Draw the first ~120 chars.\n");
			for (int i=0; i < 64; i++) {
				((JPCD8544DisplayComponent) display).LCDDrawChar((i % 14) * 6, (i/14) * 8, (char)i);
			}		
			((JPCD8544DisplayComponent) display).LCDDisplay();
			Thread.sleep(2000);
			for (int i=0; i < 64; i++)
				((JPCD8544DisplayComponent) display).LCDDrawChar((i % 14) * 6, (i/14) * 8, (char)(i + 64));

			((JPCD8544DisplayComponent) display).LCDDisplay();
			Thread.sleep(2000);
			display.clear();

		} catch (InterruptedException ie){
			ie.printStackTrace();
		}
	}
}
