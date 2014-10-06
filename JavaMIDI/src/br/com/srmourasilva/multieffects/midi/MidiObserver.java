package br.com.srmourasilva.multieffects.midi;

import javax.sound.midi.MidiMessage;

public interface MidiObserver {
	public void update(MidiMessage message);
}
