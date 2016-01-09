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
		Pedal footswitch1 = createPedal(0, RaspiPin.GPIO_17, RaspiPin.GPIO_10);
		Pedal footswitch2 = createPedal(1, RaspiPin.GPIO_27, RaspiPin.GPIO_09);
		Pedal footswitch3 = createPedal(2, RaspiPin.GPIO_22, RaspiPin.GPIO_11);
		
		Pedalboard pedalboard = new Pedalboard();
		pedalboard.add(footswitch1, footswitch2, footswitch3);
		
		return pedalboard;
	}

	private Pedal createPedal(int id, Pin buttonPin, Pin ledPin) {
		Button button = buttonFor(buttonPin);
		Light led = ledFor(ledPin);

		return new Pedal(id, button, led);
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
