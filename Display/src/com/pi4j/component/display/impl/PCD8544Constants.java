package com.pi4j.component.display.impl;

import com.pi4j.component.display.utils.Command;

class PCD8544Constants {

	enum SysCommand implements Command {
		/** Display control. */
		DISPLAY(0x08), 
		/** Function set */
		FUNC   (0x20),
		/** Set Y address of RAM */
		YADDR  (0x40),
		/** Set Y address of RAM */
		XADDR  (0x80),

		/** Temperature control */
		TEMP   (0x04),
		/** Bias system */
		BIAS   (0x10),
		/** Set Vop */
		VOP    (0x80); 

		private byte command;

		private SysCommand(int command) {
			this.command = (byte) command;
		}

		@Override
		public byte cmd() {
			return command;
		}
	};
	
	enum Setting implements Command {
		/** Sets display configuration */
		DISPLAY_E	(0x01),
		/** Sets display configuration */
		DISPLAY_D	(0x04),

		/** Extended instruction set */
		FUNC_H		(0x01),
		/** Entry mode */
		FUNC_V		(0x02),
		/** Power down control */
		FUNC_PD		(0x04),

		/** Set bias system */
		BIAS_BS0	(0x01),
		/** Set bias system */
		BIAS_BS1	(0x02),
		/** Set bias system */
		BIAS_BS2	(0x04);


		private byte command;

		private Setting(int command) {
			this.command = (byte) command;
		}

		@Override
		public byte cmd() {
			return command;
		}
	}

	interface DisplaySize {
		public static final int WIDTH  = 84;
		public static final int HEIGHT = 48;
	}

	interface RamSize {
		public static final int DDRAM_WIDTH  = DisplaySize.WIDTH;
		public static final int DDRAM_HEIGHT = DisplaySize.HEIGHT / 8;
		public static final int DDRAM_SIZE   = DDRAM_WIDTH * DDRAM_HEIGHT;
	}

	enum BitOrderFirst {
		LSB,
		MSB
	}

	public static void setAllValues(Object[] objects, Object value) {
		for (int i = 0; i < objects.length; i++)
			objects[i] = value;
	}
}
