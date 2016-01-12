package com.jpcd8544;

import java.awt.Point;

import com.pi4j.component.display.WhiteBlackDisplay;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;

/*
 Name		 : PCD8544.java
 Version     : 1

 Copyright (C) 2010 Limor Fried, Adafruit Industries
 CORTEX-M3 version by Le Dang Dung, 2011 LeeDangDung@gmail.com (tested on LPC1769)
 Raspberry Pi version by Andre Wussow, 2012, desk@binerry.de
 Raspberry Pi Java version by Cleverson dos Santos Assis, 2013, tecinfcsa@yahoo.com.br
 Raspberry Pi 2 Java version by Paulo Mateus Moura da Silva, 2016, mateus.moura@hotmail.com

 Description :
     A simple PCD8544 LCD (Nokia3310/5110) driver. Target board is Raspberry Pi.
     This driver uses 5 GPIOs on target board with a bit-bang SPI implementation
     (hence, may not be as fast).
	 Makes use of WiringPI-library of Gordon Henderson (https://projects.drogon.net/raspberry-pi/wiringpi/)

	 Recommended connection (http://www.raspberrypi.org/archives/384):
	 LCD pins      Raspberry Pi
	 LCD1 - GND    P06  - GND
	 LCD2 - VCC    P01 - 3.3V
	 LCD3 - CLK    P16 - GPIO4
	 LCD4 - Din    P12 - GPIO1
	 LCD5 - D/C    P15 - GPIO3
	 LCD6 - CS     P11 - GPIO0
	 LCD7 - RST    P13 - GPIO2
	 LCD8 - LED    P01 - 3.3V 

 References  :
 http://www.arduino.cc/playground/Code/PCD8544
 http://ladyada.net/products/nokia5110/
 http://code.google.com/p/meshphone/

================================================================================
This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.
*/

public class JPCD8544DisplayComponent implements WhiteBlackDisplay {

	private final int OUTPUT = 0xA0;
	private final int HIGH = 1;
	private final int LOW = 0;

	@Deprecated
	public final int BLACK = 1;
	@Deprecated
	public final int WHITE = 0;

	private static final int WIDTH  = 84;
	private static final int HEIGHT = 48;

	public final int PCD8544_POWERDOWN =0x04;
	public final int PCD8544_ENTRYMODE =0x02;
	public final int PCD8544_EXTENDEDINSTRUCTION =0x01;

	private final static int PCD8544_DISPLAY_BLANK = 0x0;
	private final static int PCD8544_DISPLAY_NORMAL = 0x4;
	private final static int PCD8544_DISPLAY_ALLON = 0x1;
	private final static int PCD8544_DISPLAY_INVERTED = 0x5;

	// H = 0
	private static final int PCD8544_FUNCTIONSET = 0x20;
	private static final int PCD8544_DISPLAYCONTROL = 0x08;
	private static final int PCD8544_SETYADDR = 0x40;
	private static final int PCD8544_SETXADDR = 0x80;

	// H = 1
	private static final int PCD8544_SETTEMP = 0x04;
	private static final int PCD8544_SETBIAS = 0x10;
	private static final int PCD8544_SETVOP = 0x80;


	//calibrate clock constants
	private final int CLKCONST_1  = 8000;
	private final int CLKCONST_2  = 400;  // 400 is a good tested value for Raspberry Pi

	// keywords
	private final int LSBFIRST  =0;
	private final int MSBFIRST  =1;


	// LCD port variables
	private Point cursor;

	private int textsize;
	private Color textcolor;

	private GpioPinDigitalOutput _din, _sclk, _dc, _rst, _cs;


	private boolean[][] buffer = new boolean[WIDTH][HEIGHT];
	//private int pcd8544_buffer[] = new int[WIDTH * HEIGHT / 8];

	
	private int _BV(int bit) {
		return (0x1 << (bit));
	}
	
	public void updateBoundingBox(int xmin, int ymin, int xmax, int ymax) {
	//FIXME For the 0.1 port version, I'll ignore the partialUpdate
//		#ifdef enablePartialUpdate
//		if (xmin < xUpdateMin) xUpdateMin = xmin;
//		if (xmax > xUpdateMax) xUpdateMax = xmax;
//		if (ymin < yUpdateMin) yUpdateMin = ymin;
//		if (ymax > yUpdateMax) yUpdateMax = ymax;
//		#endif
	}

	
	/**
	 * Initializes the PCD8544 LCD with default GPIO Pins
	 * 
	 * @see documentation
	 * @param contrast
	 */
	public void LCDInit(int contrast) {
		GpioController gpio = GpioFactory.getInstance();

		LCDInit(
			gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01),
			gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04), 
			gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03),
			gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02), 
			gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00),
			contrast
		);
	}
	
	/**
	 * Initialize the PCD8544 LCD for user defined GPIO pins
	 * 
	 * @param dinPin
	 * @param sclkPin
	 * @param dcPin
	 * @param rstPin
	 * @param csPin
	 * @param contrast
	 * 
	 * @deprecated Passar para construtor
	 */
	@Deprecated
	public void LCDInit(GpioPinDigitalOutput dinPin, 
						GpioPinDigitalOutput sclkPin, 
						GpioPinDigitalOutput dcPin, 
						GpioPinDigitalOutput rstPin,
						GpioPinDigitalOutput csPin,
						int contrast) {
		
		_din = dinPin;
		_sclk = sclkPin;
		_dc = dcPin;
		_rst = rstPin;
		_cs = csPin;
		
		cursor = new Point(0, 0);
		textsize = 1;
		textcolor = Color.BLACK;


		// toggle RST low to reset; CS low so it'll listen to us
		//if (_cs > 0)
			digitalWrite(_cs, LOW);

		digitalWrite(_rst, LOW);
		delayMs(500);
		digitalWrite(_rst, HIGH);

		// get into the EXTENDED mode!
		LCDCommand(PCD8544_FUNCTIONSET | PCD8544_EXTENDEDINSTRUCTION );

		// LCD bias select (4 is optimal?)
		LCDCommand(PCD8544_SETBIAS | 0x4);

		// set VOP
		if (contrast > 0x7f)
			contrast = 0x7f;

		LCDCommand( PCD8544_SETVOP | contrast); // Experimentally determined

		// normal mode
		LCDCommand(PCD8544_FUNCTIONSET);

		// Set display to Normal
		LCDCommand(PCD8544_DISPLAYCONTROL | PCD8544_DISPLAY_NORMAL);

		// set up a bounding box for screen updates
		updateBoundingBox(0, 0, WIDTH-1, HEIGHT-1);
	}

	public void LCDDrawBitmap(int x, int y, int[] bitmap, int w, int h, Color color) {
		Jpcd8544Drawer.drawBitmap(this, new Point(x, y), bitmap, w, h, color);
	}

	public void LCDDrawString(int x, int y, String str) {
		this.cursor.move(x, y);

		for (int i=0 ; i<str.length(); i++)
			LCDWrite((byte) str.charAt(i));	
	}

	/**
	 * @deprecated Tirar daqui 
	 */
	@Deprecated
	public void LCDDrawChar(int x, int y, char c) {
		if (y >= HEIGHT) return;
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

		updateBoundingBox(x, y, x+5, y + 8);
	}

	public void LCDWrite(int c) {
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

		cursor.move(x, y);
	}

	public void drawLine(int x0, int y0, int x1, int y1, Color color) {
		Jpcd8544Drawer.drawLine(this, new Point(x0, y0), new Point(x1, y1), color);
	}

	public void fillRect(int x, int y, int width, int height, Color color) {
		Jpcd8544Drawer.fillRect(this, new Point(x, y), width, height, color);
	}

	public void drawRect(int x, int y, int width, int height, Color color) {
		Jpcd8544Drawer.drawRect(this, new Point(x, y), width, height, color);
	}

	public void drawCircle(int x0, int y0, int radius, Color color)	{
		Jpcd8544Drawer.drawCircle(this, new Point(x0, y0), radius, color);
	}

	public void fillCircle(int x0, int y0, int radius, Color color) {
		Jpcd8544Drawer.fillCircle(this, new Point(x0, y0), radius, color);
	}

	// the most basic function, set a single pixel
	public void LCDSetPixel(int x, int y, int color) {
		if ((x >= WIDTH) || (y >= HEIGHT))
			return;

		// x is which column
		if (color == 1)
			pcd8544_buffer[x+ (y/8)*WIDTH] |= _BV(y%8);
		else
			pcd8544_buffer[x+ (y/8)*WIDTH] &= ~_BV(y%8);
		updateBoundingBox(x,y,x,y);
	}

	// the most basic function, get a single pixel
	public int LCDGetPixel(int x, int y){
		if ((x >= WIDTH) || (y >= HEIGHT))
			return 0;

		return (pcd8544_buffer[x+ (y/8)*WIDTH] >> (7-(y%8))) & 0x1;
	}

	public void LCDCommand(int c)	{
		digitalWrite(_dc, LOW);
		LCDSpiWrite(c);
	}

	public void LCDSpiWrite(long c)	{
		shiftOut(_din, _sclk, MSBFIRST, c);
	}

	public void LCDData(int c)	{
		digitalWrite(_dc, HIGH);
		LCDSpiWrite(c);
	}

	public void LCDSetContrast(int val) {
		if (val > 0x7f)
			val = 0x7f;

		LCDCommand(PCD8544_FUNCTIONSET | PCD8544_EXTENDEDINSTRUCTION);
		LCDCommand(PCD8544_SETVOP | val);
		LCDCommand(PCD8544_FUNCTIONSET);
	}

	public void LCDDisplay() {
		int col, maxcol, p;

		for (p = 0; p < 6; p++) {
//	#ifdef enablePartialUpdate
//			// check if this page is part of update
//			if (yUpdateMin >= ((p+1)*8))
//				continue;   // nope, skip it!

//			if (yUpdateMax < p*8)
//				break;
//	#endif

			LCDCommand(PCD8544_SETYADDR | p);


//	#ifdef enablePartialUpdate
//			col = xUpdateMin;
//			maxcol = xUpdateMax;
//	#else
			// start at the beginning of the row
			col = 0;
			maxcol = WIDTH-1;
//	#endif

			LCDCommand(PCD8544_SETXADDR | col);

			for(; col <= maxcol; col++)
				LCDData(pcd8544_buffer[(WIDTH*p)+col]);
		}

		LCDCommand(PCD8544_SETYADDR);  // no idea why this is necessary but it is to finish the last byte?
//	#ifdef enablePartialUpdate
//		xUpdateMin = LCDWIDTH - 1;
//		xUpdateMax = 0;
//		yUpdateMin = LCDHEIGHT-1;
//		yUpdateMax = 0;
//	#endif
	}

	// bitbang serial shift out on select GPIO pin. Data rate is defined by CPU clk speed and CLKCONST_2. 
	// Calibrate these value for your need on target platform.
	public void shiftOut(GpioPinDigitalOutput dataPin, GpioPinDigitalOutput clockPin, int bitOrder, long val)	{
		
		//	C version - doesn�t work in Java because the !)@*#(@! bitwise NOT operator
		// 		In java we have the complement ~ operator, but it�s not bitwise NOT
		//		Don�t you agree? Try to compile this code with C with a random number. Put the same number
		//		in Java. View the results.
		//
		//		long i, j;
		//		
		//		for (i = 0; i < 8; i++)  {
		//			if (bitOrder == LSBFIRST)
		//				digitalWrite(dataPin, ~(val & (1 << i)));
		//			else
		//				digitalWrite(dataPin, ~(val & (1 << (7-i))));
		//				
		//			digitalWrite(clockPin, HIGH);
		//			for (j = CLKCONST_2; j > 0; j--); // clock speed, anyone? (LCD Max CLK input: 4MHz)
		//			digitalWrite(clockPin, LOW);
		//		}
		long i, j;
		
		for (i = 0; i < 8; i++)  {
			if (bitOrder == LSBFIRST)
				digitalWrite(dataPin, ((val & (1 << i)) == Math.pow(2, i))?HIGH:LOW);
			else
				digitalWrite(dataPin, ((val & (1 << (7-i))) == Math.pow(2, (7-i)))?HIGH:LOW);
				
			digitalWrite(clockPin, HIGH);
			// FIXME WTF?!
			for (j = CLKCONST_2; j > 0; j--); // clock speed, anyone? (LCD Max CLK input: 4MHz)
			digitalWrite(clockPin, LOW);
		}

	}
	
	// roughly calibrated spin delay
	private void delayMs(int t)	{
		int nCount = 0;
		while (t != 0) {
			nCount = CLKCONST_1;
			while(nCount != 0)
				nCount--;
			t--;
		}
	}
	
	/**
	 * A pi4j port of arduino digitalWrite
	 * 
	 * @param pin
	 * @param state
	 */
	public void digitalWrite(GpioPinDigitalOutput pin, long state) {
		if (state == HIGH)
			pin.high();
		else
			pin.low();
		
//		try {
//			//100ms to delay. Slow, but in the initial stage
//			//Thread.sleep(100);
//		}catch (InterruptedException ie) {
//			ie.printStackTrace();
//		}
		
	}
	
	
	////////////////////////////////////////
	
	@Override
	public int getWidth() {
		return WIDTH;
	}

	@Override
	public int getHeight() {
		return HEIGHT;
	}
	


	@Override
	public void setPixel(Color color) {
		setPixel(cursor.x, cursor.y, color);
	}

	// reduces how much is refreshed, which speeds it up!
	// originally derived from Steve Evans/JCW's mod but cleaned up and optimized
	@Override
	public void setPixel(int x, int y, Color color) {
		if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT)
			return;

		// x is which column
		if (color == Color.BLACK)
			pcd8544_buffer[x+ (y/8)*WIDTH] |= _BV(y%8);
		else
			pcd8544_buffer[x+ (y/8)*WIDTH] &= ~_BV(y%8);
	}



	@Override
	public Point getCursorPosition() {
		return this.cursor.getLocation();
	}


	@Override
	public void setCursorPosition(int x, int y) {
		this.cursor.move(x, y);
	}

	@Override
	public void clear() {
		buffer = new boolean[WIDTH][HEIGHT];
		cursor = new Point(0, 0);

		updateBoundingBox(0, 0, WIDTH-1, HEIGHT-1);
	}
}
