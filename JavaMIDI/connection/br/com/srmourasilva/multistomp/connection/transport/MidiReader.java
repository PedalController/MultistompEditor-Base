package br.com.srmourasilva.multistomp.connection.transport;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import br.com.srmourasilva.architecture.exception.DeviceNotFoundException;
import br.com.srmourasilva.domain.PedalType;

/*
 * http://stackoverflow.com/questions/6937760/java-getting-input-from-midi-keyboard
 */
public class MidiReader extends MidiTransmition implements Receiver {

	public static interface MidiReaderListenner {
		void onDataReceived(MidiMessage message);
	}
	
	private MidiReaderListenner listenner;

	public MidiReader(PedalType pedalType) throws DeviceNotFoundException {
		super(pedalType);
		
		MidiDevice device = device();

		try {
			vincule(device, this);
		} catch (MidiUnavailableException e) {
			throw new DeviceNotFoundException(e);
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

	/*************************************************/

	public void setListenner(MidiReaderListenner listenner) {
		this.listenner = listenner;
	}

	@Override public void close() {}

	@Override
	public void send(MidiMessage message, long arg1) {
		listenner.onDataReceived(message);
	}
}