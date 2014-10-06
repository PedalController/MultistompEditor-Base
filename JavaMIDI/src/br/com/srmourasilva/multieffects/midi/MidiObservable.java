package br.com.srmourasilva.multieffects.midi;

import javax.sound.midi.MidiMessage;

public interface MidiObservable {
	void addObserver(MidiObserver observer);
	void updateAll(MidiMessage message);
}