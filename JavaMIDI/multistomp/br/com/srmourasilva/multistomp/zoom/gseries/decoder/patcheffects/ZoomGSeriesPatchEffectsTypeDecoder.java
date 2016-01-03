package br.com.srmourasilva.multistomp.zoom.gseries.decoder.patcheffects;

import javax.sound.midi.MidiMessage;

import br.com.srmourasilva.domain.message.Messages;
import br.com.srmourasilva.domain.multistomp.Multistomp;
import br.com.srmourasilva.multistomp.connection.codification.MessageDecoder;
import br.com.srmourasilva.multistomp.zoom.gseries.ZoomG3V2Pedals;

public class ZoomGSeriesPatchEffectsTypeDecoder implements MessageDecoder {

	@Override
	public boolean isForThis(MidiMessage message) {
		return true;
	}

	@Override
	public Messages decode(MidiMessage message, Multistomp multistomp) {
		System.out.println("Pedals type:");
		System.out.println("First :" + (message.getMessage()[11] >> 1));
		System.out.println("First :" + (ZoomG3V2Pedals.getPedals().get(message.getMessage()[11] >> 1)));

		return Messages.Empty();
	}
}
