package br.com.srmourasilva.zoom.effect;

import java.util.ArrayList;
import java.util.List;

public class Patch {
	private int id;
	private String name = "";
	private List<Effect> effects = new ArrayList<Effect>();

	public Patch(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public List<Effect> getEffects() {
		return effects;
	}

	@Override
	public String toString() {
		StringBuffer retorno = new StringBuffer();
		retorno.append("Patch ");
		retorno.append(id);
		retorno.append(" - ");
		retorno.append(name);

		return retorno.toString();
	}
}
