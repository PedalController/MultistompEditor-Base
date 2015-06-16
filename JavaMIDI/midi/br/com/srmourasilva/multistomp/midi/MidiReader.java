package br.com.srmourasilva.multistomp.midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import br.com.srmourasilva.architecture.exception.DeviceNotFoundException;
import br.com.srmourasilva.multieffects.PedalType;

/*
 * http://stackoverflow.com/questions/6937760/java-getting-input-from-midi-keyboard
 */
public class MidiReader extends MidiTransmition {

	public MidiReader(PedalType pedalType, Receiver receiver) throws DeviceNotFoundException {
		super(pedalType);
		
		MidiDevice device = device();

		try {
			vincule(device, receiver);
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

	private void vincule(MidiDevice device, Receiver receiver) throws MidiUnavailableException {
        for (Transmitter transmitter : device.getTransmitters())
        	transmitter.setReceiver(receiver);

        device.getTransmitter().setReceiver(receiver);
	}

	@Override
	protected boolean isThis(MidiDevice device) {
		return device.getMaxTransmitters() != 0;
	}

	@Override
	protected String deviceType() {
		return "input";
	}
}