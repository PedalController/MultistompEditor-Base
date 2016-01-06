package br.com.srmourasilva.multistomp.connection.transport;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

import br.com.srmourasilva.architecture.exception.DeviceNotFoundException;
import br.com.srmourasilva.domain.PedalType;

/**
 * Send the messages to real Multistomp
 */
public class MidiSender extends MidiTransmition {

	public MidiSender(PedalType pedalType) throws DeviceNotFoundException {
		super(pedalType);
	}

	@Override
	protected boolean isThis(MidiDevice device) {
		return device.getMaxReceivers() != 0;
	}

	@Override
	protected String deviceType() {
		return "output";
	}

	public void send(MidiMessage message) {
		Receiver receiver;
		try {
			receiver = device().getReceiver();
			receiver.send(message, -1);

		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}
}