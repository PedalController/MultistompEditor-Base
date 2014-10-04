package br.com.srmourasilva.zoom.midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import br.com.srmourasilva.zoom.Pedal;
import br.com.srmourasilva.zoom.architecture.Observer;

public class MidiSender implements Observer {
	private MidiDevice inputDevice;
	private MidiDevice outputDevice;

	public MidiSender(Pedal pedal) {
		try {
			searchMidiPedalController(pedal);
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		pedal.addObserver(this);
	}

	private void searchMidiPedalController(Pedal pedal) throws MidiUnavailableException {
		Info[] infos = MidiSystem.getMidiDeviceInfo();
		Info device;

		for (int i=0;i<infos.length;i++)	{
			device = infos[i];
			//System.out.println(device + " -" + device.getDescription());
			if (device.getName().contains(pedal.getUSBName())) {

				if (inputDevice == null) {
					inputDevice = MidiSystem.getMidiDevice(device);
				}
				if (outputDevice == null) {
					outputDevice = MidiSystem.getMidiDevice(device);
				}

				
				if (inputDevice.getMaxTransmitters() == 0) {
					inputDevice = null;
				}
				if (outputDevice.getMaxReceivers() == 0) {
					outputDevice = null;
				}
			}
		}
	}

	public void start() {
		try {
			inputDevice.open();
			outputDevice.open();

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
			
			for (byte mensagem : message.getMessage()) {
				System.out.println(String.format("0x%H", mensagem));
			}

		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}
		
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
/*
class ThreadTest extends Thread {
	MidiSender sender;
	public ThreadTest(MidiSender sender) {
		super();
		this.sender = sender; 
	}
		 
	public void run() {
		try {
			sleep(5000);
			this.sender.stop();

		} catch (InterruptedException e) {
			
		}
		System.out.println("Stopped!");
	}
}*/