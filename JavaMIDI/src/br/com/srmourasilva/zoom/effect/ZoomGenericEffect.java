package br.com.srmourasilva.zoom.effect;

public class ZoomGenericEffect implements Effect {
	private int midiId;
	private String name;
	private boolean state = false;
	
	public ZoomGenericEffect(int midiId, String name) {
		this.midiId = midiId;
		this.name = name;
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
}