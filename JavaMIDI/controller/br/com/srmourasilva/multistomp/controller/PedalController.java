package br.com.srmourasilva.multistomp.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.SysexMessage;

import br.com.srmourasilva.architecture.OnChangeListenner;
import br.com.srmourasilva.architecture.exception.DeviceNotFoundException;
import br.com.srmourasilva.domain.message.ChangeMessage;
import br.com.srmourasilva.domain.message.MessageDecoder;
import br.com.srmourasilva.domain.message.MessageEncoder;
import br.com.srmourasilva.domain.multistomp.Multistomp;
import br.com.srmourasilva.multistomp.midi.MidiReader;
import br.com.srmourasilva.multistomp.midi.MidiSender;
import br.com.srmourasilva.multistomp.midi.MidiTransmition;
import br.com.srmourasilva.multistomp.simulator.Log;
import br.com.srmourasilva.multistomp.zoom.gseries.ZoomGSeriesMessageDecoder;
import br.com.srmourasilva.multistomp.zoom.gseries.ZoomGSeriesMessageEncoder;

public class PedalController implements OnChangeListenner<Multistomp> {

	private boolean stated;
	private Multistomp pedal;
	private MidiSender sender;
	private MidiReader reader;

	public PedalController(Multistomp pedal) throws DeviceNotFoundException {
		this.pedal = pedal;
		this.sender = new MidiSender(pedal.getPedalType());
		this.reader = new MidiReader(pedal.getPedalType(), new MidiInputReceiver());

		this.pedal.addListenner(this);
		this.pedal.addListenner(new Log());
	}

	/*************************************************/

	/** Turn on and inicialize the pedal
	 */
	public final void on() throws MidiUnavailableException {
		if (stated)
			return;

		stated = true;
		pedal.initialize();

		sender.start();
		reader.start();
	}

	/** Close connection and turn off the pedal
	 */
	public final void off() {
		if (!stated)
			return;

		stated = false;
		pedal.terminate();

		sender.stop();
		reader.stop();
	}


	/*************************************************/

	public final boolean getState() {
		return stated;
	}

	protected final Multistomp getPedal() {
		return pedal;
	}


	/*************************************************/

	public void nextPatch() {
		this.pedal.nextPatch();
	}

	public void beforePatch() {
		this.pedal.beforePatch();
	}

	public void setPatch(int index) {
		this.pedal.toPatch(index);
	}


	/*************************************************/

	public void activeEffect(int idEffect) {
		this.pedal.currentPatch().effects().get(idEffect).active();
	}

	public void disableEffect(int idEffect) {
		this.pedal.currentPatch().effects().get(idEffect).disable();
	}

	/** Set an param to 'newValue' of effect with the 'idEffect' given
	 * FIXME
	 */
	//public abstract void setEffectParam(int idEffect, int newValue);

	/** @return Amount of effects that the current patch has
	 */
	public final int getAmountEffects() {
		return this.pedal.currentPatch().effects().size();
	}


	/*************************************************/
	public final String toString() {
		String retorno = "State: ";
		retorno += stated ? "On" : "Off";
		retorno += "\n";

		return retorno + this.pedal.toString();
	}


	@Override
	public void onChange(ChangeMessage<Multistomp> message) {
		List<MidiMessage> midiMessages = generateMidiMessage(message);

		for (MidiMessage Midimessage : midiMessages)
			this.sender.send(Midimessage);
	}

	private List<MidiMessage> generateMidiMessage(ChangeMessage<Multistomp> message) {
		MessageEncoder encoder = new ZoomGSeriesMessageEncoder();

		return encoder.encode(message);
	}

	@Deprecated
	public void sendMessage(SysexMessage sysexMessage) {
		this.sender.send(sysexMessage);
	}

	/*************************************************/

	public class MidiInputReceiver implements Receiver {
		
		private MessageDecoder decoder;

		public MidiInputReceiver() {
			this.decoder = new ZoomGSeriesMessageDecoder();
		}
		
	    public void send(MidiMessage message, long timeStamp) {
	    	System.out.println("MIDI received: ");
	    	System.out.println(" " + MidiTransmition.byteArrayToHex(message.getMessage()));

	    	System.out.println(" " + decoder.getClass().getSimpleName());
	    	System.out.print(" > ");

	    	Collection<OnChangeListenner<Multistomp>> listenners = new ArrayList<>(pedal.listenners());
	    	pedal.listenners().clear();

	    	decoder.decode(message, pedal);

	    	for (OnChangeListenner<Multistomp> listenner : listenners)
	    		pedal.addListenner(listenner);
	    }

	    public void close() {}
    }
}