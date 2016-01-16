package com.pi4j.component.display.impl;

import java.util.Iterator;

import com.pi4j.component.display.WhiteBlackDisplay.Color;

class PCB8544DDramBank {
	private final int x;
	private final int y;
	private Color[] colors;
	private boolean changed;

	public PCB8544DDramBank(int x, int y, Color initialColor) {
		this.x = x;
		this.y = y;

		this.changed = false;
		this.colors = new Color[8];
		for (int i = 0; i < colors.length; i++)
			this.colors[i] = initialColor;
	}

	public void setPixel(int y, Color color) {
		if (colors[y] != color)
			this.changed = true;

		colors[y] = color;
	}
	
	public Color getPixel(int y) {
		return colors[y];
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public boolean hasChanged() {
		return this.changed;
	}

	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}
	
	public Iterator<Color> lsbIterator() {
		return null;
	}
	
	public Iterator<Color> msbIterator() {
		return new MsbIterator(this);
	}

	private static class MsbIterator implements Iterator<Color> {
		private PCB8544DDramBank PCB8544DisplayDDramBank;
		private int count;

		public MsbIterator(PCB8544DDramBank PCB8544DisplayDDramBank) {
			this.PCB8544DisplayDDramBank = PCB8544DisplayDDramBank;
			this.count = 7;
		}

		@Override
		public Color next() {
			return PCB8544DisplayDDramBank.getPixel(count--);
		}

		@Override
		public boolean hasNext() {
			return count >= 0;
		}
	}
}