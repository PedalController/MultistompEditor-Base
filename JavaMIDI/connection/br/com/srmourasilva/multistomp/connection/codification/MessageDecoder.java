package br.com.srmourasilva.multistomp.connection.codification;

import javax.sound.midi.MidiMessage;

import br.com.srmourasilva.domain.message.ChangeMessage;
import br.com.srmourasilva.domain.multistomp.Multistomp;

/** 
 * Decode a MidiMessage in ChangeMessage<Multistomp>
 */
public interface MessageDecoder {
	boolean isForThis(MidiMessage message);
	ChangeMessage<Multistomp> decode(MidiMessage message, Multistomp multistomp);
}
