package br.com.srmourasilva.domain.message;

import javax.sound.midi.MidiMessage;

import br.com.srmourasilva.domain.multistomp.Multistomp;

public interface MessageDecoder {
	boolean isForThis(MidiMessage message);
	void decode(MidiMessage message, Multistomp multistomp);
}
