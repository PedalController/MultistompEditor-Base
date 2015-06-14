package br.com.srmourasilva.domain.message;

import java.util.List;

import javax.sound.midi.MidiMessage;

import br.com.srmourasilva.domain.multistomp.Multistomp;

public interface MessageEncoder {
	List<MidiMessage> encode(ChangeMessage<Multistomp> message);
}
