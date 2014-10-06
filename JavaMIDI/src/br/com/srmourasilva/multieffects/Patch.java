package br.com.srmourasilva.multieffects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.srmourasilva.architecture.ObserverObservable;

/** Patch observa seus Effects
 *  Patch é observado pelo Pedal
 */
public class Patch extends ObserverObservable {
	private int id;
	private String name = "";
	private List<Effect> effects = new ArrayList<Effect>();

	public Patch(int id) {
		this.id = id;
	}

	public final int getId() {
		return id;
	}

	public final List<Effect> getEffects() {
		return effects;
	}

	public final void addEffect(Effect effect) {
		this.effects.add(effect);
	}

	public final void addAllEffects(Collection<Effect> effects) {
		this.effects.addAll(effects);
	}

	@Override
	public final String toString() {
		StringBuffer retorno = new StringBuffer();
		retorno.append("Patch ");
		retorno.append(id);
		retorno.append(" - ");
		retorno.append(name);
		retorno.append("(" + effects.size() + " Effect(s))");

		return retorno.toString();
	}
}