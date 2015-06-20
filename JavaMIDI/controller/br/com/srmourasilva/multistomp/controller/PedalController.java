package br.com.srmourasilva.multistomp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.SysexMessage;

import br.com.srmourasilva.architecture.exception.DeviceNotFoundException;
import br.com.srmourasilva.domain.OnChangeListenner;
import br.com.srmourasilva.domain.message.ChangeMessage;
import br.com.srmourasilva.domain.multistomp.Multistomp;
import br.com.srmourasilva.multistomp.connection.MidiConnection;
import br.com.srmourasilva.multistomp.connection.MidiConnection.OnUpdateListenner;
import br.com.srmourasilva.multistomp.simulator.Log;

public class PedalController implements OnChangeListenner<Multistomp>, OnUpdateListenner {

	private boolean started;

	private Multistomp pedal;

	private MidiConnection connection;

	private List<OnChangeListenner<Multistomp>> controllerListenners = new ArrayList<>();
	private List<OnChangeListenner<Multistomp>> realMultistompListenners = new ArrayList<>();

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
	public final int getAmountEffects() {
		return this.pedal.currentPatch().effects().size();
	}

	public void addListenner(OnChangeListenner<Multistomp> listenner) {
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
	public synchronized void onChange(ChangeMessage<Multistomp> message) {
		if (realChange) {
			realChange = false;
			return;
		}

		connection.send(message);
		notify(controllerListenners, message);
	}

	private volatile boolean realChange = false;

	/** Real multistomp Change */
	@Override
	public synchronized void update(ChangeMessage<Multistomp> message) {
		this.realChange = true;

		MultistompChanger changer = new MultistompChanger(this);
		changer.attempt(message);

		notify(realMultistompListenners, message);
	}

	private void notify(List<OnChangeListenner<Multistomp>> controllerListenners, ChangeMessage<Multistomp> message) {
		for (OnChangeListenner<Multistomp> listenner : controllerListenners)
			listenner.onChange(message);
	}

	@Deprecated
	public void sendMessage(SysexMessage sysexMessage) {
		this.connection.send(sysexMessage);
	}
}