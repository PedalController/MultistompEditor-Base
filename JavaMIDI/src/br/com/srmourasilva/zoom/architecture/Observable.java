package br.com.srmourasilva.zoom.architecture;

import javax.sound.midi.MidiMessage;

public interface Observable {
	void addObserver(Observer observer);
	void updateAll(MidiMessage message);
}