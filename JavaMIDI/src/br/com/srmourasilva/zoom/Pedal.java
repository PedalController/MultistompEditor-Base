package br.com.srmourasilva.zoom;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiMessage;

import br.com.srmourasilva.zoom.architecture.Observable;
import br.com.srmourasilva.zoom.architecture.Observer;
import br.com.srmourasilva.zoom.effect.Effect;
import br.com.srmourasilva.zoom.effect.Patch;

/**
 * A pedal is a Multi-Effects Processing
 */
public abstract class Pedal implements Observable {

	List<Observer> observers = new ArrayList<Observer>();

	private List<Patch> patchs = new ArrayList<Patch>();

	private List<Effect> effects = new ArrayList<Effect>();

	int idCurrentPatch;

	/** Inicializate Pedal */
	protected abstract void init();

	/** Return the USBName Device
	 * 
	 * The name will be used to find out which is the USB which is connected to the PC
	 * that is corresponding Pedal
	 * @return
	 */
	public abstract String getUSBName();

	/*************************************************/

	protected final void addPatch(Patch patch) {
		patchs.add(patch);
	}

	public final Patch getCurrentPatch() {
		return patchs.get(idCurrentPatch);
	}

	public final void nextPatch() {
		this.setPatch(idCurrentPatch+1);
	}

	public final void beforePatch() {
		this.setPatch(idCurrentPatch-1);
	}

	/** Set the current Pedal patch to 'index' Patch
	 */
	public final void setPatch(int index) {
		if (index >= patchs.size()) {
			index = 0;

		} else if (index < 0) {
			index = patchs.size()-1;
		}

		MidiMessage message = getMessageSetPatch(index);

		this.updateAll(message);
		idCurrentPatch = index;
	}

	/** Return the MIDI message that will be change the Set Patch
	 */
	protected abstract MidiMessage getMessageSetPatch(int idPatch);

	/*************************************************/

	/** Create Effects that will be populated in the Pedal.
	 * These Effects are to be activated/deactivated 
	 */
	protected abstract List<Effect> createEffects();

	protected final void setEffects(List<Effect> effects) {
		this.effects = effects;
	}

	public final void activeEffect(int index) {
		setStatePedal(index, true);
	}

	public final void disableEffect(int index) {
		setStatePedal(index, false);
	}

	private final void setStatePedal(int index, boolean state) {
		if (index >= effects.size() || index < 0) {
			return;
		}

		this.setStatePedal(index, state, effects.get(index));
	}

	/**
	 * @param index  Index in pedal (pedaleira)
	 * @param state  New state
	 * @param effect Effect that will be updated 
	 */
	private final void setStatePedal(int index, boolean state, Effect effect) {
		effect.setState(state);

		for (MidiMessage message : getMessagesSetState(index, state, effect)) {
			this.updateAll(message);
		}
	}

	/** Return the MIDI message that will be change the Effect Status
	 * 
	 * @param index  Index of Effect in Pedal
	 * @param state  New state
	 * @param effect Effect that will be changed state 
	 */
	protected abstract List<MidiMessage> getMessagesSetState(int index, boolean state, Effect effect);

	/*************************************************/

	public final void addObserver(Observer observer) {
		observers.add(observer);
	}

	/** Notify observers that have a MidiMessage to be sent
	 */
	@Override
	public final void updateAll(MidiMessage message) {
		for (Observer observer : observers) {
			observer.update(message);
		}
	}

	/*************************************************/

	@Override
	public final String toString() {
		StringBuffer retorno = new StringBuffer();
		retorno.append("Pedaleira: "   + this.getClass().getSimpleName() + "\n");
		retorno.append(" - Current Patch: " + this.getCurrentPatch().toString() + "\n");
		retorno.append(" - Effects: \n");
		for (Effect effect : effects) {
			retorno.append("  |- " + effect.toString() + "\n");
		}
	
		return retorno.toString();
	}
}