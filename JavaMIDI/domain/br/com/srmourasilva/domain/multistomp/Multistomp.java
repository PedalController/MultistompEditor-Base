package br.com.srmourasilva.domain.multistomp;

import java.util.ArrayList;
import java.util.List;

import br.com.srmourasilva.architecture.exception.ImplemetationException;
import br.com.srmourasilva.domain.OnMultistompListener;
import br.com.srmourasilva.domain.message.Messages;
import br.com.srmourasilva.domain.multistomp.message.ChangeMessage;
import br.com.srmourasilva.domain.multistomp.message.Details;
import br.com.srmourasilva.domain.multistomp.message.Details.TypeChange;
import br.com.srmourasilva.domain.multistomp.message.MultistompCause;
import br.com.srmourasilva.domain.multistomp.message.OnChangeListener;

public abstract class Multistomp implements OnChangeListener<Patch> {

	private List<OnMultistompListener> listeners = new ArrayList<>();

	private List<Patch> patchs = new ArrayList<Patch>();

	private int idCurrentPatch = 0;

	protected void addPatch(Patch patch) {
		patchs.add(patch);
		patch.setListener(this);
	}

	public Patch currentPatch() {
		try {
			return patchs.get(idCurrentPatch);
		} catch (IndexOutOfBoundsException e) {
			throw new ImplemetationException("This multistomp havent any Patch. \nAdd the Patchs in the Pedal Construtor: " + this.getClass().getCanonicalName());
		}
	}

	public int getIdCurrentPatch() {
		return idCurrentPatch;
	}

	public void nextPatch() {
		this.toPatch(idCurrentPatch+1);
	}

	public void beforePatch() {
		this.toPatch(idCurrentPatch-1);
	}

	public void toPatch(int index) {
		if (index >= patchs.size())
			index = 0;

		else if (index < 0)
			index = patchs.size()-1;

		idCurrentPatch = index;
		
		Details<Integer> details = new Details<>(TypeChange.PATCH_NUMBER, idCurrentPatch);

		ChangeMessage<Multistomp> newMessage = new ChangeMessage<>(MultistompCause.MULTISTOMP, this, details);
		this.notify(newMessage);
	}

	public List<Patch> patchs() {
		return patchs;
	}
	/*************************************************/

	@Override
	public final String toString() {
		StringBuffer retorno = new StringBuffer();
		retorno.append("Multistomp: "  + this.getClass().getSimpleName() + "\n");
		retorno.append(" - Current Patch: " + this.currentPatch().toString() + "\n");
		retorno.append(" - Effects: \n");

		for (Effect effect : this.currentPatch().effects())
			retorno.append("  |- " + effect.toString() + "\n");

		return retorno.toString();
	}


	/*************************************************/

	public void addListener(OnMultistompListener listener) {
		this.listeners.add(listener);
	}
	
	public List<OnMultistompListener> listeners() {
		return this.listeners;
	}

	@Override
	public void onChange(ChangeMessage<Patch> message) {
		ChangeMessage<Multistomp> newMessage = new ChangeMessage<>(MultistompCause.SUPER, this, message);
		notify(newMessage);
	}

	private void notify(ChangeMessage<Multistomp> message) {
		Messages messages = MultistompMessagesConverter.convert(message);

		listeners.forEach(listener -> listener.onChange(messages));
	}

	/*************************************************/

	public abstract Messages start();
}