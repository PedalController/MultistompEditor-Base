package br.com.srmourasilva.main;

import br.com.srmourasilva.architecture.exception.DeviceNotFoundException;
import br.com.srmourasilva.multieffects.PedalType;
import br.com.srmourasilva.multistomp.midi.MidiReader;

public class ReceiverMain {
	public static void main(String[] args) {
		try {
			new MidiReader(PedalType.G3);
		} catch (DeviceNotFoundException e) {
			e.printStackTrace();
		}
	}
}
