package br.com.srmourasilva.multistomp.connection.codification;

import java.util.List;

import javax.sound.midi.MidiMessage;

import br.com.srmourasilva.domain.message.Messages;

/** 
 * Generate the MidiMessage based in the changes
 * described in ChangeMessage<Multistomp>
 */
public interface MessageEncoder {
	List<MidiMessage> encode(Messages messages);
}
