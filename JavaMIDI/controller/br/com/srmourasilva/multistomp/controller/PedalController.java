package br.com.srmourasilva.multistomp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.SysexMessage;

import br.com.srmourasilva.architecture.exception.DeviceNotFoundException;
import br.com.srmourasilva.domain.OnMultistompListenner;
import br.com.srmourasilva.domain.message.Messages;
import br.com.srmourasilva.domain.multistomp.Multistomp;
import br.com.srmourasilva.multistomp.connection.MidiConnection;
import br.com.srmourasilva.multistomp.connection.MidiConnection.OnUpdateListenner;
import br.com.srmourasilva.multistomp.simulator.Log;

public class PedalController implements OnMultistompListenner, OnUpdateListenner {

	private boolean started;

	private Multistomp pedal;

	private MidiConnection connection;

	private List<OnMultistompListenner> controllerListenners = new ArrayList<>();
	private List<OnMultistompListenner> realMultistompListenners = new ArrayList<>();

	public PedalController(Multistomp pedal) throws DeviceNotFoundException {
		this.pedal = pedal;
		
		this.connection = new MidiConnection(pedal, pedal.getPedalType());
		this.connection.setListenner(this);

		this.pedal.addListenner(this);

		this.controllerListenners.add(new Log("Controller"));
		this.realMultistompListenners.add(new Log("Real Multistomp"));
	}

	/*************************************************/

	/** Turn on and inicialize the pedal
	 */
	public final void on() throws MidiUnavailableException {
		if (started)
			return;

		started = true;
		connection.start();

		connection.send(pedal.start());
		realChange = false; // FIXME - GAMBIARRA
		//onChange(message)
		//this.connection.send(message)
		//notify(realMultistompListenners, message)
		//sleep();
	}
	
	public void sleep() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/** Close connection and turn off the pedal
	 */
	public final void off() {
		if (!started)
			return;

		started = false;
		connection.stop();
	}

	/*************************************************/

	public final boolean getState() {
		return started;
	}

	public final Multistomp multistomp() {
		return pedal;
	}


	/*************************************************/

	public void nextPatch() {
		this.pedal.nextPatch();
	}

	public void beforePatch() {
		this.pedal.beforePatch();
	}

	public void toPatch(int index) {
		this.pedal.toPatch(index);
	}


	/*************************************************/

	public void toogleEffect(int idEffect) {
		this.pedal.currentPatch().effects().get(idEffect).toggle();	
	}

	public boolean hasActived(int idEffect) {
		return this.pedal.currentPatch().effects().get(idEffect).hasActived();
	}

	public void activeEffect(int idEffect) {
		this.pedal.currentPatch().effects().get(idEffect).active();
	}

	public void disableEffect(int idEffect) {
		this.pedal.currentPatch().effects().get(idEffect).disable();
	}

	public void setEffectParam(int idEffect, int idParam, int value) {
		this.pedal.currentPatch().effects().get(idEffect).params().get(idParam).setValue(value);
	}

	/** @return Amount of effects that the current patch has
	 */
	public int getAmountEffects() {
		return this.pedal.currentPatch().effects().size();
	}

	public void addListenner(OnMultistompListenner listenner) {
		this.pedal.addListenner(listenner);
	}

	/*************************************************/
	public final String toString() {
		String retorno = "State: ";
		retorno += started ? "On" : "Off";
		retorno += "\n";

		return retorno + this.pedal.toString();
	}

	/** Multistomp Change */
	@Override
	public synchronized void onChange(Messages messages) {
		if (realChange) {
			realChange = false;
			return;
		}

		connection.send(messages);
		notify(controllerListenners, messages);
	}

	private volatile boolean realChange = false;

	/** Real multistomp Change */
	@Override
	public synchronized void update(Messages messages) {
		this.realChange = true;

		MultistompChanger changer = new MultistompChanger(this);
		messages.forEach(message -> changer.attempt(message));

		notify(realMultistompListenners, messages);
	}

	private void notify(List<OnMultistompListenner> listenners, Messages messages) {
		for (OnMultistompListenner listenner : listenners)
			listenner.onChange(messages);
	}

	@Deprecated
	public void sendMessage(SysexMessage sysexMessage) {
		this.connection.send(sysexMessage);
	}
	
	public void send(Messages messages) {
		this.connection.send(messages);
		this.realChange = true;
	}
}