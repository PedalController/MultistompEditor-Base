package com.pi4j.component.display.utils;

public class ByteCommand implements Command {
	private byte command;

	public ByteCommand(int command) {
		this.command = (byte) command;
	}

	@Override
	public byte cmd() {
		return command;
	}
}