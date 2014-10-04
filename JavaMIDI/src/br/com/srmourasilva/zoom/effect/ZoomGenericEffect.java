package br.com.srmourasilva.zoom.effect;

import java.util.ArrayList;
import java.util.List;

import br.com.srmourasilva.zoom.effect.param.Param;

// TODO - Converter para abstract class
public class ZoomGenericEffect implements Effect {
	private int midiId;
	private String name;
	private boolean state = false;
	private int effectsLength;

	private List<Param> params = new ArrayList<Param>();
	
	public ZoomGenericEffect(int midiId, String name, int effectsLength) {
		this.midiId = midiId;
		this.name = name;
		this.effectsLength = effectsLength;
	}

	@Override
	public int getMidiId() {
		return midiId;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void active() {
		state = true;
	}

	@Override
	public void disable() {
		state = false;
	}

	@Override
	public boolean getState() {
		return state;
	}

	@Override
	public void setState(boolean state) {
		this.state = state;
	}

	@Override
	public String toString() {
		String retorno = this.getClass().getSimpleName() + ": "+ midiId + " " + name + " - ";
		retorno += state ? "Actived" : "Disabled";

		return retorno;
	}

	@Override
	public Param getParam(int id) {
		return params.get(id);
	}

	@Override
	public void setParamValue(int id, int value) {
		params.get(id).setValue(value);
	}
}