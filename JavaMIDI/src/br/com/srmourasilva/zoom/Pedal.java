package br.com.srmourasilva.zoom;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiMessage;
import br.com.srmourasilva.zoom.architecture.Observable;
import br.com.srmourasilva.zoom.architecture.Observer;
import br.com.srmourasilva.zoom.effect.Effect;
import br.com.srmourasilva.zoom.effect.Patch;

public abstract class Pedal implements Observable {

	List<Observer> observers = new ArrayList<Observer>();

	private List<Patch> patchs = new ArrayList<Patch>();
	int idCurrentPatch;

	public abstract String getUSBName();


	protected void addPatch(Patch patch) {
		patchs.add(patch);
	}


	public Patch getCurrentPatch() {
		return patchs.get(idCurrentPatch);
	}
	public void nextPatch() {
		this.setPatch(idCurrentPatch+1);
	}
	public void beforePatch() {
		this.setPatch(idCurrentPatch-1);
	}
	/** Template Method
	 */
	public final void setPatch(int index) {
		if (index >= patchs.size()) {
			index = 0;

		} else if (index < 0) {
			index = patchs.size()-1;
		}

		this.setPatchImpl(index);
		idCurrentPatch = index;
	}
	protected abstract void setPatchImpl(int index);
	

	public void addObserver(Observer observer) {
		observers.add(observer);
	}
	@Override
	public void updateAll(MidiMessage message) {
		for (Observer observer : observers) {
			observer.update(message);
		}
	}

	/** Popule the pedal with pedal Effects to active/disable 
	 * @return */
	protected abstract List<Effect> createEffects();
	public abstract void activeEffect(int index);
	public abstract void disableEffect(int index);
	/**
	 * @param index  Index in pedal (pedaleira)
	 * @param state  New state
	 * @param effect Effect that will be updated 
	 */
	protected abstract void setStatePedal(int index, boolean state, Effect effect);

	@Override
	public String toString() {
		StringBuffer retorno = new StringBuffer();
		retorno.append("Pedaleira: "   + this.getClass().getSimpleName() + "\n");
		retorno.append("Patch Atual: " + this.getCurrentPatch().toString());

		return retorno.toString();
	}

	/** Inicializate pedal */
	protected abstract void init();
}