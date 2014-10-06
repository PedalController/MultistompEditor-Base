package br.com.srmourasilva.multieffects;

/** Effect observa seus Params
 *  Effect é observado pelo Patch
 */
import java.util.ArrayList;
import java.util.List;

import br.com.srmourasilva.architecture.ObserverObservable;

public abstract class Effect extends ObserverObservable {

	private int midiId;
	private String name;
	private boolean state = false;

	private List<Param> params = new ArrayList<Param>();


	protected Effect(int midiId, String name) {
		this.midiId = midiId;
		this.name = name;
	}

	/** Midi Id for send message */
	public final int getMidiId() {
		return midiId;
	}

	public final String getName() {
		return name;
	}

	public final void active() {
		setState(true);
	}

	public final void disable() {
		setState(false);
	}

	public final void setState(boolean state) {
		this.state = state;
		updateAll(this);
	}

	public final boolean getState() {
		return state;
	}
	
	public final void addParam(Param param) {
		this.params.add(param);
	}

	public String toString() {
		String retorno = this.getClass().getSimpleName() + ": "+ midiId + " " + name + " - ";
		retorno += state ? "Actived" : "Disabled";

		return retorno;
	}
}