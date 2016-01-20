package br.com.srmourasilva.ppc.mvp;

import com.pi4j.component.display.drawer.DisplayGraphics;

public interface View {
	/**
	 * Claims the view (re)draw all elements in display
	 */
	void paint(DisplayGraphics graphics);
}
