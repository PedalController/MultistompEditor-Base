package br.com.srmourasilva.simplepedalcontroller.business;

import br.com.srmourasilva.simplepedalcontroller.domain.Pedal;
import br.com.srmourasilva.simplepedalcontroller.domain.Pedalboard;

import com.pi4j.component.button.Button;
import com.pi4j.component.button.impl.GpioButtonComponent;
import com.pi4j.component.light.Light;
import com.pi4j.component.light.impl.GpioLightComponent;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public class PedalboardBuilder {
	
	public enum Multistomp {
		ZoomG3;
	}
	
	private GpioController gpio;

	public PedalboardBuilder(GpioController gpio) {
		this.gpio = gpio;
	}
	
	public Pedalboard generateFor(Multistomp multistomp) {
		Pedal footswitch1 = createPedal(RaspiPin.GPIO_00, RaspiPin.GPIO_01);
		Pedal footswitch2 = createPedal(RaspiPin.GPIO_02, RaspiPin.GPIO_05);
		Pedal footswitch3 = createPedal(RaspiPin.GPIO_04, RaspiPin.GPIO_06);
		
		Pedalboard pedalboard = new Pedalboard();
		pedalboard.add(footswitch1, footswitch2, footswitch3);
		
		return pedalboard;
	}

	private Pedal createPedal(Pin buttonPin, Pin ledPin) {
		Button button = buttonFor(buttonPin);
		Light led = ledFor(ledPin);
		
		return new Pedal(button, led);
	}

	private Button buttonFor(Pin pin) {
		GpioPinDigitalInput inputPin = gpio.provisionDigitalInputPin(pin);
		return new GpioButtonComponent(inputPin);
	}

	private Light ledFor(Pin pin) {
		GpioPinDigitalOutput outputPin = gpio.provisionDigitalOutputPin(pin);
		return new GpioLightComponent(outputPin);
	}
}
