package br.com.srmourasilva.simplepedalcontroller.domain;

import javax.sound.midi.MidiUnavailableException;

import br.com.srmourasilva.multistomp.controller.PedalController;
import br.com.srmourasilva.simplepedalcontroller.domain.clicable.Clicable;

public class PhysicalPedalController {

	private PedalController controller;

	public PhysicalPedalController(PedalController controller) {
		this.controller = controller;
	}

	public void vinculePedalEffects(PhysicalEffect ... pedals) {
		for (PhysicalEffect pedal : pedals)
			pedal.setListener(effect -> controller.toogleEffect(effect));
	}

	public void vinculeNext(Clicable next) {
		next.setListener(clicable -> controller.nextPatch());
	}

	public void vinculeBefore(Clicable next) {
		next.setListener(clicable -> controller.beforePatch());
	}

	public void start() throws MidiUnavailableException {
		controller.on();
	}
}