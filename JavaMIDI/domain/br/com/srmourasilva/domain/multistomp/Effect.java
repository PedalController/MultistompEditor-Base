package br.com.srmourasilva.domain.multistomp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.srmourasilva.architecture.OnChangeListenner;
import br.com.srmourasilva.domain.message.ChangeMessage;
import br.com.srmourasilva.domain.message.CommonCause;

public class Effect implements OnChangeListenner<Param> {

	private int midiId;
	private String name;
	private boolean state = false;

	private List<Param> params = new ArrayList<Param>();

	private Optional<OnChangeListenner<Effect>> listenner = Optional.empty();

	public Effect(int midiId, String name) {
		this.midiId = midiId;
		this.name = name;
	}

	/** Midi Id for send message */
	public int getMidiId() {
		return midiId;
	}

	public String getName() {
		return name;
	}

	public void active() {
		setState(true);
	}

	public void disable() {
		setState(false);
	}
	
	public void toggle() {
		if (hasActived())
			disable();
		else
			active();
	}

	private void setState(boolean state) {
		this.state = state;

		ChangeMessage<Effect> message = new ChangeMessage<>(CommonCause.EFFECT, this);
		notify(message);
	}

	public boolean hasActived() {
		return state;
	}
	
	public void addParam(Param param) {
		this.params.add(param);
		param.setOnChangeListenner(this);
	}

	public List<Param> params() {
		return this.params;
	}

	public String toString() {
		String retorno = this.getClass().getSimpleName() + ": "+ midiId + " " + name + " - ";
		retorno += state ? "Actived" : "Disabled";

		return retorno;
	}

	/*************************************************/

	public void setOnChangeListenner(OnChangeListenner<Effect> listenner) {
		this.listenner = Optional.of(listenner);
	}

	@Override
	public void onChange(ChangeMessage<Param> message) {
		ChangeMessage<Effect> newMessage = new ChangeMessage<>(CommonCause.SUPER, this, message);
		notify(newMessage);	
	}
	
	private void notify(ChangeMessage<Effect> message) {
		if (!listenner.isPresent())
			return;

		listenner.get().onChange(message);
	}
}