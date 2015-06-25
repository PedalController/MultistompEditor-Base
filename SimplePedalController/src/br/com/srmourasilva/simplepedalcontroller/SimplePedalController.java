package br.com.srmourasilva.simplepedalcontroller;

import br.com.srmourasilva.simplepedalcontroller.business.PedalboardBuilder;
import br.com.srmourasilva.simplepedalcontroller.controller.PedalInterface;
import br.com.srmourasilva.simplepedalcontroller.domain.Pedalboard;
import br.com.srmourasilva.simplepedalcontroller.presenter.Presenter;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

public class SimplePedalController {

	public static void main(String[] args) {
		GpioController gpio = GpioFactory.getInstance();

		PedalboardBuilder builder = new PedalboardBuilder(gpio);
		Pedalboard zoom = builder.generateFor(PedalboardBuilder.Multistomp.ZoomG3);

		PedalInterface pedalInterface = new PedalInterface(zoom);
		Presenter presenter = new Presenter(pedalInterface);

		presenter.start();
	}
}
