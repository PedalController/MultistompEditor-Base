package com.pi4j.component.display.impl;

import java.awt.Point;
import java.util.Iterator;
import java.util.Queue;

import com.pi4j.component.display.WhiteBlackDisplay;
import com.pi4j.component.display.impl.PCD8544Constants.BitOrderFirst;
import com.pi4j.component.display.impl.PCD8544Constants.DisplaySize;
import com.pi4j.component.display.impl.PCD8544Constants.Setting;
import com.pi4j.component.display.impl.PCD8544Constants.SysCommand;
import com.pi4j.component.display.utils.ByteCommand;
import com.pi4j.component.display.utils.Command;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.wiringpi.Gpio;

/* 
	Raspberry Pi 2 Java version
	Copyright (C) 2015 by Paulo Mateus Moura da Silva, 2016, mateus.moura[at]hotmail[dot]com

	Based in 2013 Giacomo Trudu - wicker25[at]gmail[dot]com
	Based in 2010 Limor Fried, Adafruit Industries
	Based in CORTEX-M3 version by Le Dang Dung, 2011 LeeDangDung@gmail.com (tested on LPC1769)
	Based in  Raspberry Pi version by Andre Wussow, 2012, desk@binerry.de
	Based in  Raspberry Pi Java version by Cleverson dos Santos Assis, 2013, tecinfcsa@yahoo.com.br
	
	This library is free software; you can redistribute it and/or
	modify it under the terms of the GNU Lesser General Public
	License as published by the Free Software Foundation; either
	version 2.1 of the License, or (at your option) any later version.

	This library is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
	Lesser General Public License for more details.

	You should have received a copy of the GNU Lesser General Public License
	along with Rpi-hw. If not, see <http://www.gnu.org/licenses/>. 
*/
public class PCD8544DisplayComponent implements WhiteBlackDisplay {

	private static final int CLOCK_TIME_DELAY = 100;//micro seconds
	//http://stackoverflow.com/questions/11498585/how-to-suspend-a-java-thread-for-a-small-period-of-time-like-100-nanoseconds
	private static final int RESET_DELAY = 1;//10^-3ms

	private PCB8544DDRam DDRAM;

	/** Serial data input. */
	private GpioPinDigitalOutput DIN;
	/** Input for the clock signal */
	private GpioPinDigitalOutput SCLK;
	/** Data/Command mode select */
	private GpioPinDigitalOutput DC; 
	/** External rst input */
	private GpioPinDigitalOutput RST; 
	/** Chip Enable (CS/SS) */
	private GpioPinDigitalOutput SCE; 

	/**
	 * 
	 * @param din Serial data input.
	 * @param sclk Input for the clock signal.
	 * @param dc Data/Command mode select.
	 * @param rst External rst input.
	 * @param cs Chip Enable (CS/SS)
	 * 
	 * @param contrast
	 * @param inverse
	 */
	public PCD8544DisplayComponent(
			GpioPinDigitalOutput din, 
			GpioPinDigitalOutput sclk, 
			GpioPinDigitalOutput dc, 
			GpioPinDigitalOutput rst,
			GpioPinDigitalOutput cs,

			byte contrast,
			boolean inverse) {

		this.DDRAM = new PCB8544DDRam(this, Color.WHITE);

		this.DIN = din;
		this.SCLK = sclk;
		this.DC = dc;
		this.RST = rst;
		this.SCE = cs;

		reset();
		init(contrast, inverse);
		redraw();
	}

	private void reset() {
		RST.low();
		try {
			Thread.sleep(RESET_DELAY);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		RST.high();
	}

	private void init(byte contrast, boolean inverse) {
		sendCommand(SysCommand.FUNC, Setting.FUNC_H);
		sendCommand(SysCommand.BIAS, new ByteCommand(0x04));
		sendCommand(SysCommand.VOP, new ByteCommand(contrast & 0x7f ));
		sendCommand(SysCommand.FUNC);
		sendCommand(
			SysCommand.DISPLAY,
			Setting.DISPLAY_D,
			new ByteCommand(Setting.DISPLAY_E.cmd() * (byte) (inverse ? 1 : 0))
		);
	}

	/**
	 * @param Send command | command | command...
	 */
	private void sendCommand(Command ... commands) {
		byte result = 0;

		for (Command command : commands)
			result |= command.cmd();

		sendCommand(result);
	}

	private void sendCommand(byte data) {
		DC.low();

		SCE.low();
		writeData(data);
		SCE.high();
	}

	private void writeData(byte data) {
		BitOrderFirst order = BitOrderFirst.MSB;
		if (order == BitOrderFirst.MSB)
			writeDataMSBFirst(data);
		else
			writeDataLSBFirst(data);
	}

	private void writeDataLSBFirst(byte data) {
		for (byte i = 0; i < 8; ++i) {
			PinState bitState = (data & (1 << i)) >> i == 1 ? PinState.HIGH : PinState.LOW;
			DIN.setState(bitState);

			toggleClock();
		}
	}

	private void writeDataMSBFirst(byte data) {
		for (byte i = 7; i >= 0; --i) {
			PinState bitState = (data & (1 << i)) >> i == 1 ? PinState.HIGH : PinState.LOW;
			DIN.setState(bitState);

			toggleClock();
		}
	}

	private void toggleClock() {
		SCLK.high();
		Gpio.delayMicroseconds(CLOCK_TIME_DELAY);
		SCLK.low();
	}

	public void setContrast(byte value) {
		sendCommand(SysCommand.FUNC, Setting.FUNC_H);
		sendCommand(SysCommand.VOP, new ByteCommand(value & 0x7f));
		sendCommand(SysCommand.FUNC);
	}

	@Override
	public void setPixel(int x, int y, Color color) {
		this.DDRAM.setPixel(x, y, color);
	}

	public boolean isPositionExists(int x, int y) {
		return !(x < 0 || y < 0 || x >= getWidth() || y >= getHeight());
	}

	public Color getPixel(int x, int y) { 
		return this.DDRAM.getPixel(x, y);
	}

	@Override
	public void redraw() {
		Point cursor = new Point(-1, -1);

		Queue<PCB8544DDramBank> changes = this.DDRAM.getChanges();
		System.out.println("Start redraw: " + changes.size() + " changes");
		while (!changes.isEmpty()) {
			PCB8544DDramBank bank = changes.remove();
			if (cursor.y != bank.y()) {
				cursor.y = bank.y();
				setCursorY(cursor.y);
			}

			if (cursor.x != bank.x()) {
				cursor.x = bank.x();
				setCursorX(cursor.x);
			}

			sendData(bank);
		}
		System.out.println("End redraw");
	}

	private void sendData(PCB8544DDramBank bankData) {
		DC.high();

		SCE.low();
		writeData(bankData);
		SCE.high();
	}

	private void writeData(PCB8544DDramBank bank) {
		Iterator<Color> iterator = bank.msbIterator();
		while (iterator.hasNext()) {
			Color color = iterator.next();
			DIN.setState(color == Color.BLACK ? true : false);

			toggleClock();
		}
	}

	private void setCursorX(int x) {
		sendCommand(SysCommand.XADDR, new ByteCommand(x));
	}

	private void setCursorY(int y) {
		sendCommand(SysCommand.YADDR, new ByteCommand(y));
	}

	public void clear() {
		this.DDRAM.clear();

		setCursorX(0);
		setCursorY(0);
	}
	
	@Override
	public int getWidth() {
		return DisplaySize.WIDTH;
	}
	
	@Override
	public int getHeight() {
		return DisplaySize.HEIGHT;
	}
}
