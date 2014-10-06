package br.com.srmourasilva.multieffects.controller;

import br.com.srmourasilva.multieffects.Pedal;
import br.com.srmourasilva.multieffects.midi.MidiSender;

/** Facade to control the Pedal
 */
public abstract class PedalController {

	private boolean state;
	private Pedal pedal;
	private MidiSender sender;

	public PedalController(Pedal pedal) {
		this.pedal = pedal;
		this.sender = new MidiSender(pedal);
		this.pedal.addObserver(this.sender);
	}


	/*************************************************/

	/** Turn on and inicialize the pedal
	 */
	public final void on() {
		if (state) {
			return;
		}
		state = true;
		pedal.initialize();
		sender.start();
	}

	/** Close connection and turn off the pedal
	 */
	public final void off() {
		if (!state) {
			return;
		}
		state = false;
		pedal.terminate();
		sender.stop();
	}


	/*************************************************/

	public final boolean getState() {
		return state;
	}

	protected final Pedal getPedal() {
		return pedal;
	}


	/*************************************************/

	public abstract void nextPatch();

	public abstract void beforePatch();

	/** Set the current patch to patch of id given
	 */
	public abstract void setPatch(int id);


	/*************************************************/

	public abstract void activeEffect(int idEffect);

	public abstract void disableEffect(int idEffect);

	/** Set an param to 'newValue' of effect with the 'idEffect' given
	 */
	public abstract void setEffectParam(int idEffect, int newValue);

	/** @return Amount of effects that the current patch has
	 */
	public final int getAmountEffects() {
		return this.pedal.getCurrentPatch().getEffects().size();
	}


	/*************************************************/
	public final String toString() {
		String retorno = "State: ";
		retorno += state ? "On" : "Off";
		retorno += "\n";

		return retorno + this.pedal.toString();
	}
}