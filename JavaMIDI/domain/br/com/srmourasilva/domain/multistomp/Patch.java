package br.com.srmourasilva.domain.multistomp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.srmourasilva.domain.multistomp.message.ChangeMessage;
import br.com.srmourasilva.domain.multistomp.message.Details;
import br.com.srmourasilva.domain.multistomp.message.Details.TypeChange;
import br.com.srmourasilva.domain.multistomp.message.MultistompCause;
import br.com.srmourasilva.domain.multistomp.message.OnChangeListener;

public class Patch implements OnChangeListener<Effect> {
	private int id;
	private String name = "";
	private List<Effect> effects = new ArrayList<Effect>();

	private Optional<OnChangeListener<Patch>> listener = Optional.empty();

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
		effect.setListener(this);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;

		Details<String> details = new Details<>(TypeChange.PATCH_NAME, name);

		ChangeMessage<Patch> newMessage = new ChangeMessage<Patch>(MultistompCause.PATCH, this, details);
		notify(newMessage);
	}

	/*************************************************/

	public void setListener(OnChangeListener<Patch> listener) {
		this.listener = Optional.of(listener);
	}

	@Override
	public void onChange(ChangeMessage<Effect> message) {
		ChangeMessage<Patch> newMessage = new ChangeMessage<>(MultistompCause.SUPER, this, message);
		notify(newMessage);
	}

	private void notify(ChangeMessage<Patch> message) {
		if (!listener.isPresent())
			return;

		listener.get().onChange(message);
	}

	/*************************************************/

	@Override
	public final String toString() {
		StringBuffer retorno = new StringBuffer();
		retorno.append("Patch ");
		retorno.append(id);
		retorno.append(" - ");
		retorno.append(name);
		retorno.append(" (" + effects.size() + " Effect(s))");

		return retorno.toString();
	}
}