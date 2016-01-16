package com.pi4j.component.display.impl;

import java.util.LinkedList;
import java.util.Queue;

import com.pi4j.component.display.WhiteBlackDisplay.Color;
import com.pi4j.component.display.impl.PCD8544Constants.DisplaySize;

class PCB8544DDRam {

	interface DDRamSize {
		public static final int DDRAM_WIDTH  = DisplaySize.WIDTH;
		public static final int DDRAM_HEIGHT = DisplaySize.HEIGHT / 8;
		public static final int DDRAM_SIZE   = DDRAM_WIDTH * DDRAM_HEIGHT;
	}


	private PCB8544DDramBank[][] buffer = new PCB8544DDramBank[DDRamSize.DDRAM_WIDTH][DDRamSize.DDRAM_HEIGHT];

	private final PCD8544DisplayComponent display;
	private final Color initialColor;

	private Queue<PCB8544DDramBank> changes;
	
	public PCB8544DDRam(PCD8544DisplayComponent display, Color initialColor) {
		this.display = display;
		this.initialColor = initialColor;
		
		this.changes = new LinkedList<>();

		for (int x = 0; x < DDRamSize.DDRAM_WIDTH; x++) {
			for (int y = 0; y < DDRamSize.DDRAM_HEIGHT; y++) {
				buffer[x][y] = new PCB8544DDramBank(x, y, initialColor);
				this.changes.add(buffer[x][y]);
			}
		}
	}

	public void setPixel(int x, int y, Color color) {
		if (!isPositionExists(x, y))
			throw new IndexOutOfBoundsException("Position ("+x+", "+y+") don't exists");

		PCB8544DDramBank bank = getBank(x, y);
		boolean changedAnotherTime = bank.hasChanged(); 

		bank.setPixel(y%8, color);

		if (bank.hasChanged() && !changedAnotherTime)
			this.changes.add(bank);
	}

	private PCB8544DDramBank getBank(int x, int y) {
		PCB8544DDramBank bank = buffer[x][y/8];
		//if (bank == null) {
			//bank = new PCB8544DDramBank(x, y/8, this.initialColor);
			//buffer[x][y/8] = bank;
		//}

		return bank;
	}

	public Color getPixel(int x, int y) {
		if (!isPositionExists(x, y))
			throw new IndexOutOfBoundsException("Position ("+x+", "+y+") don't exists");

		return this.getBank(x, y).getPixel(y);
	}

	private boolean isPositionExists(int x, int y) {
		return !(x < 0 || y < 0 || x >= display.getWidth() || y >= display.getHeight());
	}

	public void clear() {
		for (int x = 0; x < PCD8544Constants.DisplaySize.WIDTH; x++)
			for (int y = 0; y < PCD8544Constants.DisplaySize.HEIGHT; y++)
				setPixel(x, y, initialColor);
	}
	
	public Queue<PCB8544DDramBank> getChanges() {
		return changes;
	}
}