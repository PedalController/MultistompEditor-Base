package br.com.srmourasilva.zoom.tests;

import br.com.srmourasilva.zoom.Pedal;
import br.com.srmourasilva.zoom.ZoomFactory;
import br.com.srmourasilva.zoom.ZoomFactory.PedalType;
import br.com.srmourasilva.zoom.midi.MidiSender;

/**
 * http://www.onicos.com/staff/iz/formats/midi-event.html
 *
 */
public class Main {
	public static void main(String[] args) {
		Pedal pedaleira = ZoomFactory.getPedal(PedalType.G3);

		MidiSender sender = new MidiSender(pedaleira);
		sender.start();

		pedaleira.setPatch(0);

		for (int i = 0; i < 6; i++) {
			pedaleira.activeEffect(i);
		//	pedaleira.disableEffect(i);
		}

		// Mute
		//pedaleira.activeEffect(8);
		// Bypass
		//pedaleira.activeEffect(9);

		// Set Patch
		//pedaleira.beforePatch();
		//pedaleira.nextPatch();
		//pedaleira.setPatch(99);

		System.out.println(pedaleira);

		sender.stop();
	}
}
