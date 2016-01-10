package br.com.srmourasilva.simplepedalcontroller;

import javax.sound.midi.MidiUnavailableException;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;

import br.com.srmourasilva.architecture.exception.DeviceNotFoundException;
import br.com.srmourasilva.multistomp.controller.PedalController;
import br.com.srmourasilva.multistomp.controller.PedalControllerFactory;
import br.com.srmourasilva.simplepedalcontroller.domain.PhysicalEffect;
import br.com.srmourasilva.simplepedalcontroller.domain.PhysicalPedalController;
import br.com.srmourasilva.simplepedalcontroller.domain.clicable.Clicable;

/**
 * Simple pedal controller by buttons or switchs in a
 * Raspberry Pi
 */
public class SimplePedalController {

	public static void main(String[] args) {
		GpioController gpio = GpioFactory.getInstance();
		Builder builder = new Builder(gpio);

		PhysicalEffect footswitch1 = new PhysicalEffect(
			0,
			//builder.buildButton(RaspiPin.GPIO_17, gpio),
			builder.buildMomentarySwitch(RaspiPin.GPIO_17),
			builder.buildLed(RaspiPin.GPIO_10)
		);

		PhysicalEffect footswitch2 = new PhysicalEffect(
			1,
			builder.buildMomentarySwitch(RaspiPin.GPIO_27),
			builder.buildLed(RaspiPin.GPIO_09)
		);

		PhysicalEffect footswitch3 = new PhysicalEffect(
			2,
			builder.buildMomentarySwitch(RaspiPin.GPIO_22),
			builder.buildLed(RaspiPin.GPIO_11)
		);


		//Clicable next   = builder.buildMomentarySwitch(RaspiPin.GPIO_28);
		//Clicable before = builder.buildMomentarySwitch(RaspiPin.GPIO_29);


		
		PedalController pedal;
		try {
			pedal = PedalControllerFactory.searchPedal();
		} catch (DeviceNotFoundException e1) {
			System.out.println("Pedal not found! You connected any?");
			return;
		}

		PhysicalPedalController multistomp = new PhysicalPedalController(pedal);
		multistomp.vinculePedalEffects(footswitch1, footswitch2, footswitch3);

		//multistomp.vinculeNext(next);
		//multistomp.vinculeBefore(before);


		try {
			multistomp.start();
		} catch (MidiUnavailableException e) {
			System.out.println("This Pedal has been used by other process program");
		}
	}
}
