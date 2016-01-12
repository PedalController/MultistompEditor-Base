package com.pi4j.component.display;

public interface WhiteBlackDisplay extends Display<WhiteBlackDisplay.Color> {
	public enum Color implements Display.Color {
		BLACK,
		WHITE;
	}
}
