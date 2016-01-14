package com.pi4j.component.display.impl;

import com.pi4j.component.display.WhiteBlackDisplay.Color;
import com.pi4j.component.display.impl.PCD8544Constants.DisplaySize;

/**
 * @deprecated Implementation "baixo nível"
 */
@Deprecated
class PCB8544DramOld {
	
	interface RamSize {
		public static final int DDRAM_WIDTH  = DisplaySize.WIDTH;
		public static final int DDRAM_HEIGHT = DisplaySize.HEIGHT / 8;
		public static final int DDRAM_SIZE   = DDRAM_WIDTH * DDRAM_HEIGHT;
	}

	private byte[] buffer     = new byte[RamSize.DDRAM_SIZE];
	private Boolean[] updates = new Boolean[RamSize.DDRAM_SIZE];

	private PCD8544DisplayComponent display;
	
	public PCB8544DramOld(PCD8544DisplayComponent display) {
		this.display = display;
		setAllValues(this.updates, true);
	}

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

	public Color getPixel(int x, int y) { 
		if (!isPositionExists(x, y))
			throw new IndexOutOfBoundsException("Position ("+x+", "+y+") don't exists");

		byte index = (byte) (y % 8);
		byte block = getPixelPosition(x, y);
		
		return (buffer[block] | 1 << index) == 1 ? Color.BLACK : Color.WHITE;
		//return utils::get_bit( m_buffer, block, 7 - ( y % 8 ) );
	}

	private boolean isPositionExists(int x, int y) {
		return !(x < 0 || y < 0 || x >= display.getWidth() || y >= display.getHeight());
	}
	
	/**
	 * @return The byte then contains the pixel
	 */
	private byte getPixelPosition(int x, int y) {
		return (byte) ((byte) x + ((byte) y / 8) * this.display.getWidth());
	}

	public void clear() {
		this.buffer = new byte[RamSize.DDRAM_SIZE];

		setAllValues(this.updates, true);
	}
	
	public static void setAllValues(Object[] objects, Object value) {
		for (int i = 0; i < objects.length; i++)
			objects[i] = value;
	}
}
