package br.com.srmourasilva.domain.multistomp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.srmourasilva.domain.multistomp.message.ChangeMessage;
import br.com.srmourasilva.domain.multistomp.message.Details;
import br.com.srmourasilva.domain.multistomp.message.Details.TypeChange;
import br.com.srmourasilva.domain.multistomp.message.MultistompCause;
import br.com.srmourasilva.domain.multistomp.message.OnChangeListener;

public class Effect implements OnChangeListener<Param> {

	private int midiId;
	private String name;
	private boolean state = false;

	private List<Param> params = new ArrayList<Param>();

	private Optional<OnChangeListener<Effect>> listener = Optional.empty();

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
		
		Details<Integer> details = new Details<>(TypeChange.PEDAL_STATUS, state ? 1 : 0);

		ChangeMessage<Effect> message = new ChangeMessage<>(MultistompCause.EFFECT, this, details);
		notify(message);
	}

	public boolean hasActived() {
		return state;
	}
	
	public void addParam(Param param) {
		this.params.add(param);
		param.setListener(this);
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

	public void setListener(OnChangeListener<Effect> listener) {
		this.listener = Optional.of(listener);
	}

	@Override
	public void onChange(ChangeMessage<Param> message) {
		ChangeMessage<Effect> newMessage = new ChangeMessage<>(MultistompCause.SUPER, this, message);
		notify(newMessage);	
	}

	private void notify(ChangeMessage<Effect> message) {
		if (!listener.isPresent())
			return;

		listener.get().onChange(message);
	}
}