package br.com.srmourasilva.zoom.architecture;

import javax.sound.midi.MidiMessage;

public interface Observer {
	public void update(MidiMessage message);
}
