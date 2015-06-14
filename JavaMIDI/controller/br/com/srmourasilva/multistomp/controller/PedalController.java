package br.com.srmourasilva.multistomp.controller;

import java.util.List;

import javax.sound.midi.MidiMessage;

import br.com.srmourasilva.architecture.OnChangeListenner;
import br.com.srmourasilva.domain.message.ChangeMessage;
import br.com.srmourasilva.domain.message.MessageEncoder;
import br.com.srmourasilva.domain.multistomp.Multistomp;
import br.com.srmourasilva.multieffects.midi.MidiSender;
import br.com.srmourasilva.multistomp.zoom.gseries.ZoomGSeriesMessageEncoder;

public class PedalController implements OnChangeListenner<Multistomp> {

	private boolean stated;
	private Multistomp pedal;
	private MidiSender sender;

	public PedalController(Multistomp pedal) {
		this.pedal = pedal;
		this.sender = new MidiSender(pedal);
		this.pedal.addListenner(this);
	}

	/*************************************************/

	/** Turn on and inicialize the pedal
	 */
	public final void on() {
		if (stated)
			return;

		stated = true;
		pedal.initialize();
		sender.start();
	}

	/** Close connection and turn off the pedal
	 */
	public final void off() {
		if (!stated)
			return;

		stated = false;
		pedal.terminate();
		sender.stop();
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
}