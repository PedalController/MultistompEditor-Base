package br.com.srmourasilva.multieffects.midi;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

import br.com.srmourasilva.architecture.DeviceNotFoundException;
import br.com.srmourasilva.multieffects.Pedal;
import br.com.srmourasilva.multieffects.PedalType;

/**
 * FIXME - Why MidiSender have a input?!
 * Criar MidiReceive - O código é o comentado
 * 
 * MidiSender observa Pedal
 */
public class MidiSender implements MidiObserver {
	private MidiDevice inputDevice;
	private MidiDevice outputDevice;
	private Pedal pedal;

	public MidiSender(Pedal pedal) {
		try {
			searchMidiPedalController(pedal);
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		this.pedal = pedal;
	}

	/** Return all devices that corresponding
	 *  the PedalType
	 */
	public static List<Info> findDevices(PedalType type) {
		List<Info> devices = new ArrayList<Info>();

		Info[] infos = MidiSystem.getMidiDeviceInfo();
		Info device;

		for (int i=0; i<infos.length; i++) {
			device = infos[i];

			if (device.getName().contains(type.getUSBName())) {
				devices.add(device);
			}
		}

		return devices;
	}

	private void searchMidiPedalController(Pedal pedal) throws MidiUnavailableException {
		List<Info> devices = findDevices(pedal.getPedalType());

		MidiDevice tempDevice;

		for (Info device : devices) {
			tempDevice = MidiSystem.getMidiDevice(device);

			if (inputDevice == null && tempDevice.getMaxTransmitters() != 0) {
				inputDevice = tempDevice;
			}
			if (outputDevice == null && tempDevice.getMaxReceivers() != 0) {
				outputDevice = MidiSystem.getMidiDevice(device);
			}
		}
	}

	public void start() {
		try {
			inputDevice.open();
			outputDevice.open();

		} catch (NullPointerException e) {
			throw new DeviceNotFoundException("Midi devices not found: " + pedal.getClass().getSimpleName() + " ("+pedal.getPedalType().getUSBName()+")");

		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		inputDevice.close();
		outputDevice.close();
	}

	@Override
	public void update(MidiMessage message) {
		Receiver receiver;
		try {
			receiver = outputDevice.getReceiver();
			receiver.send(message, -1);

			/*
			for (byte mensagem : message.getMessage()) {
				System.out.println(String.format("0x%H", mensagem));
			}
			*/

		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

	////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////

		/**
		 * http://stackoverflow.com/a/13222449/1524997
		 * http://www.onicos.com/staff/iz/formats/midi-event.html
		 */
		/*
		Transmitter transmitter;
		Receiver receiver;

		 

		transmitter = inputDevice.getTransmitter();
		receiver = sequencer.getReceiver();
		transmitter.setReceiver(receiver);

		Sequence sequence = new Sequence(Sequence.PPQ, 10);
		Track currentTrack = sequence.createTrack();

		sequencer.setSequence(sequence);
		sequencer.setTickPosition(0);
		sequencer.recordEnable(currentTrack, -1);
		sequencer.startRecording();
		sequencer.addControllerEventListener(new ControllerEventListener() {
			@Override
			public void controlChange(ShortMessage arg0) {
				System.out.println(arg0);
			}
		}, new int [] {-1});

		new ThreadTest(this).start();*/


	/*
		if (sequencer.isRecording()) {
		    sequencer.stopRecording();

		    Sequence tmp = sequencer.getSequence();
		    byte[] bytes = tmp.getTracks()[0].get(0).getMessage().getMessage();
		    for (byte b : bytes) {
				System.out.print(b);
			}

		    // Save to file
		    try {
				MidiSystem.write(tmp, 0, new File("MyMidiFile.mid"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		    sequencer.close();
		    inputDevice.close();
		}
	}*/
}