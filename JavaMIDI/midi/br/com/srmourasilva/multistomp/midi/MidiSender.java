package br.com.srmourasilva.multistomp.midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

import br.com.srmourasilva.architecture.exception.DeviceNotFoundException;
import br.com.srmourasilva.multieffects.PedalType;

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

			System.out.println(byteArrayToHex(message.getMessage()));

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