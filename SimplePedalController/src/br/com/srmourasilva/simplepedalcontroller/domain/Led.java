package br.com.srmourasilva.simplepedalcontroller.domain;

import com.pi4j.io.gpio.GpioPinDigital;

@Deprecated
public class Led {
	private GpioPinDigital pin;
	private GpioPinDigital ground;
	
	public Led(GpioPinDigital pin, GpioPinDigital ground) {
		this.pin = pin;
		this.ground = ground;
	}
}
