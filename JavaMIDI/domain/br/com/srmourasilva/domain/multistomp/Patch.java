package br.com.srmourasilva.domain.multistomp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.srmourasilva.domain.multistomp.message.ChangeMessage;
import br.com.srmourasilva.domain.multistomp.message.MultistompCause;
import br.com.srmourasilva.domain.multistomp.message.OnChangeListenner;

public class Patch implements OnChangeListenner<Effect> {
	private int id;
	private String name = "";
	private List<Effect> effects = new ArrayList<Effect>();

	private Optional<OnChangeListenner<Patch>> listenner = Optional.empty();

	public Patch(int id) {
		this.id = id;
	}

	public final int getId() {
		return id;
	}

	public final List<Effect> effects() {
		return effects;
	}

	public final void addEffect(Effect effect) {
		this.effects.add(effect);
		effect.setListenner(this);
	}

	/*************************************************/

	public void setListenner(OnChangeListenner<Patch> listenner) {
		this.listenner = Optional.of(listenner);
	}

	@Override
	public void onChange(ChangeMessage<Effect> message) {
		ChangeMessage<Patch> newMessage = new ChangeMessage<>(MultistompCause.SUPER, this, message);
		notify(newMessage);
	}

	private void notify(ChangeMessage<Patch> message) {
		if (!listenner.isPresent())
			return;

		listenner.get().onChange(message);
	}

	/*************************************************/

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