package com.pi4j.component.display.impl;

import static com.pi4j.component.display.impl.PCD8544Constants.setAllValues;

import com.pi4j.component.display.WhiteBlackDisplay;
import com.pi4j.component.display.impl.PCD8544Constants.BitOrderFirst;
import com.pi4j.component.display.impl.PCD8544Constants.DisplaySize;
import com.pi4j.component.display.impl.PCD8544Constants.RamSize;
import com.pi4j.component.display.impl.PCD8544Constants.Setting;
import com.pi4j.component.display.impl.PCD8544Constants.SysCommand;
import com.pi4j.component.display.utils.ByteCommand;
import com.pi4j.component.display.utils.Command;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

/* 
	Title --- display/pcd8544.cpp
	
	Copyright (C) 2013 Giacomo Trudu - wicker25[at]gmail[dot]com
	Raspberry Pi 2 Java version by Paulo Mateus Moura da Silva, 2016, mateus.moura@hotmail.com
	
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

	private static final int CLOCK_TIME_DELAY = 10;//ms
	private static final int RESET_DELAY = 10^-3;//ms
	
	private byte[] buffer     = new byte[RamSize.DDRAM_SIZE];
	private Boolean[] updates = new Boolean[RamSize.DDRAM_SIZE];

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

		setAllValues(this.updates, true); 

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

	private void sendData(byte data) {
		DC.high();

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
		try {
			Thread.sleep(CLOCK_TIME_DELAY);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		SCLK.low();
	}

	public void setContrast(byte value) {
		sendCommand(SysCommand.FUNC, Setting.FUNC_H);
		sendCommand(SysCommand.VOP, new ByteCommand(value & 0x7f ));
		sendCommand(SysCommand.FUNC);
	}

	@Override
	public void setPixel(int x, int y, Color color) {
		if (!isPositionExists(x, y))
			throw new IndexOutOfBoundsException("Position ("+x+", "+y+") don't exists");

		// Calculate the bit position
		byte index = (byte) (y % 8);

		byte position = getPixelPosition(x, y);

		// Set the color of the pixel
		if (color == Color.BLACK)
			buffer[position] = (byte) (buffer[position] | 1 << index);
		else
			buffer[position] = (byte) (buffer[position] & ~(1 << index));

		updates[position] = true;
	}
	
	public boolean isPositionExists(int x, int y) {
		return !(x < 0 || y < 0 || x >= getWidth() || y >= getHeight());
	}

	public Color getPixel(int x, int y) { 
		if (!isPositionExists(x, y))
			throw new IndexOutOfBoundsException("Position ("+x+", "+y+") don't exists");

		byte index = (byte) (y % 8);
		byte block = getPixelPosition(x, y);
		
		return (buffer[block] | 1 << index) == 1 ? Color.BLACK : Color.WHITE;
		//return utils::get_bit( m_buffer, block, 7 - ( y % 8 ) );
	}
	
	/**
	 * @return The byte then contains the pixel
	 */
	private byte getPixelPosition(int x, int y) {
		return (byte) ((byte) x + ((byte) y / 8) * getWidth());
	}

	// FIXME - Refactor
	@Override
	public void redraw() {
		// Flag indicating that a block is skipped
		boolean jump = true;

		int k = 0;

		// Update the pixels in the bounding box
		for (int y = 0; y < RamSize.DDRAM_HEIGHT; ++y) {
			setCursorY(y);

			for (int x = 0; x < RamSize.DDRAM_WIDTH; ++x, ++k) {
				if (updates[k]) {
					if (jump) {
						setCursorX(x);
						jump = false;
					}
	
					sendData(buffer[k]);
					updates[k] = false;
				} else jump = true;
			}
		}
	}

	private void setCursorX(int x) {
		sendCommand(SysCommand.XADDR, new ByteCommand(x));
	}

	private void setCursorY(int y) {
		sendCommand(SysCommand.YADDR, new ByteCommand(7));
	}

	public void clear() {
		this.buffer = new byte[RamSize.DDRAM_SIZE];

		setAllValues(this.updates, true);

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
