package br.com.srmourasilva.multistomp.connection.codification;

import java.util.List;

import javax.sound.midi.MidiMessage;

import br.com.srmourasilva.domain.message.ChangeMessage;
import br.com.srmourasilva.domain.multistomp.Multistomp;

/** 
 * Generate the MidiMessage based in the changes
 * described in ChangeMessage<Multistomp>
 */
public interface MessageEncoder {
	List<MidiMessage> encode(ChangeMessage<Multistomp> message);
}
