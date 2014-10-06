package br.com.srmourasilva.multieffects;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiMessage;

import br.com.srmourasilva.architecture.ImplemetationException;
import br.com.srmourasilva.architecture.Observer;
import br.com.srmourasilva.multieffects.midi.MidiObservable;
import br.com.srmourasilva.multieffects.midi.MidiObserver;

/** A pedal is a Multi-Effects Processing
 * 
 * This a FAÇADE of System
 * 
 * FIXME - Pedal não deve possuir efeitos
 * Os efeitos pertencem ao PATCH
 * 
 * Pedal observa seus Patchs
 * Pedal é observado pelo MidiSender 
 */
public abstract class Pedal implements Observer, MidiObservable {

	private List<MidiObserver> observers = new ArrayList<MidiObserver>();

	private List<Patch> patchs = new ArrayList<Patch>();

	private int idCurrentPatch = 0;


	/*************************************************/

	/** Inicializate Pedal */
	public abstract void initialize();

	/** Stop the Pedal */
	public abstract void terminate();


	/*************************************************/

	public abstract PedalType getPedalType();


	/*************************************************/

	protected final void addPatch(Patch patch) {
		patchs.add(patch);
	}

	// FIXME - Translate
	public final Patch getCurrentPatch() {
		try {
			return patchs.get(idCurrentPatch);
		} catch (IndexOutOfBoundsException e) {
			throw new ImplemetationException("Nenhum Patch foi criado. \nAdicione os Patchs no construtor do Pedal: " + this.getClass().getCanonicalName());
		}
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

	@Override
	public final String toString() {
		StringBuffer retorno = new StringBuffer();
		retorno.append("Pedaleira: "   + this.getClass().getSimpleName() + "\n");
		retorno.append(" - Current Patch: " + this.getCurrentPatch().toString() + "\n");
		retorno.append(" - Effects: \n");
		for (Effect effect : this.getCurrentPatch().getEffects()) {
			retorno.append("  |- " + effect.toString() + "\n");
		}

		return retorno.toString();
	}


	/*************************************************/

	@Override
	public final void addObserver(MidiObserver observer) {
		this.observers.add(observer);
	}


	/*************************************************/

	/** Notify observers that have a MidiMessage to be sent
	 */
	@Override
	public final void updateAll(MidiMessage message) {
		for (MidiObserver observer : observers) {
			observer.update(message);
		}
	}
}