package br.com.srmourasilva.domain.multistomp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.srmourasilva.domain.multistomp.message.ChangeMessage;
import br.com.srmourasilva.domain.multistomp.message.Details;
import br.com.srmourasilva.domain.multistomp.message.Details.TypeChange;
import br.com.srmourasilva.domain.multistomp.message.MultistompCause;
import br.com.srmourasilva.domain.multistomp.message.OnChangeListenner;

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
		
		Details details = new Details(TypeChange.PEDAL_STATUS, state ? 1 : 0);

		ChangeMessage<Effect> message = new ChangeMessage<>(MultistompCause.EFFECT, this, details);
		notify(message);
	}

	public boolean hasActived() {
		return state;
	}
	
	public void addParam(Param param) {
		this.params.add(param);
		param.setListenner(this);
	}

	public List<Param> params() {
		return this.params;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName() + ": "+ midiId + " " + name + " - ");
		builder.append(state ? "Actived" : "Disabled");

		builder.append(" ( ");
		for (Param param : params)
			builder.append(param + " ");
		builder.append(")");

		return builder.toString();
	}

	/*************************************************/

	public void setListenner(OnChangeListenner<Effect> listenner) {
		this.listenner = Optional.of(listenner);
	}

	@Override
	public void onChange(ChangeMessage<Param> message) {
		ChangeMessage<Effect> newMessage = new ChangeMessage<>(MultistompCause.SUPER, this, message);
		notify(newMessage);	
	}

	private void notify(ChangeMessage<Effect> message) {
		if (!listenner.isPresent())
			return;

		listenner.get().onChange(message);
	}
}