package br.com.srmourasilva.simplepedalcontroller.domain;

import com.pi4j.component.button.Button;
import com.pi4j.component.button.ButtonPressedListener;
import com.pi4j.component.light.Light;

public class Pedal {
	private int id;
	private Light led;
	private Button button;

	public Pedal(int id, Button button, Light led) {
		this.id = id;
		this.led = led;
		this.button = button;
	}

	public void addListener(ButtonPressedListener ... arg0) {
		this.button.addListener(arg0);
	}

	public boolean isThis(Button button) {
		return this.button == button;
	}

	public void active() {
		led.on();
	}

	public void disable() {
		led.off();
	}

	public int getId() {
		return id;
	}
}