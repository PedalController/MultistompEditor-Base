package br.com.srmourasilva.domain.multistomp;

import java.util.ArrayList;
import java.util.List;

import br.com.srmourasilva.architecture.exception.ImplemetationException;
import br.com.srmourasilva.domain.OnMultistompListenner;
import br.com.srmourasilva.domain.PedalType;
import br.com.srmourasilva.domain.message.Cause;
import br.com.srmourasilva.domain.message.CommonCause;
import br.com.srmourasilva.domain.message.Messages;
import br.com.srmourasilva.domain.multistomp.message.ChangeMessage;
import br.com.srmourasilva.domain.multistomp.message.Details;
import br.com.srmourasilva.domain.multistomp.message.Details.TypeChange;
import br.com.srmourasilva.domain.multistomp.message.MultistompCause;
import br.com.srmourasilva.domain.multistomp.message.OnChangeListenner;

public abstract class Multistomp implements OnChangeListenner<Patch> {

	private List<OnMultistompListenner> listenners = new ArrayList<>();

	private List<Patch> patchs = new ArrayList<Patch>();

	private int idCurrentPatch = 0;

	/*************************************************/

	@Deprecated
	public abstract PedalType getPedalType();


	/*************************************************/

	protected void addPatch(Patch patch) {
		patchs.add(patch);
		patch.setListenner(this);
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
		
		Details details = new Details(TypeChange.PATCH_NUMBER, idCurrentPatch);

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

	public void addListenner(OnMultistompListenner listenner) {
		this.listenners.add(listenner);
	}
	
	public List<OnMultistompListenner> listenners() {
		return this.listenners;
	}

	@Override
	public void onChange(ChangeMessage<Patch> message) {
		ChangeMessage<Multistomp> newMessage = new ChangeMessage<>(MultistompCause.SUPER, this, message);
		notify(newMessage);
	}

	private void notify(ChangeMessage<Multistomp> message) {
		Messages messages = convertToMessages(message);

		listenners.forEach(listenner -> listenner.onChange(messages));
	}

	private Messages convertToMessages(ChangeMessage<Multistomp> message) {
		Messages messages = new Messages();
		br.com.srmourasilva.domain.message.Messages.Details details = new br.com.srmourasilva.domain.message.Messages.Details();

		if (message.is(MultistompCause.MULTISTOMP)) {			
			details.patch = message.causer().getIdCurrentPatch();

			messages.add(CommonCause.TO_PATCH, details);

		} else if (message.is(MultistompCause.EFFECT)) {
			Patch patch = (Patch) message.nextMessage().causer();
			details.patch = message.causer().patchs().indexOf(patch);

			Effect effect = (Effect) message.realMessage().causer();
			int idEffect = patch.effects().indexOf(effect);
			
			details.effect = idEffect;
			Cause cause = effect.hasActived() ? CommonCause.ACTIVE_EFFECT : CommonCause.DISABLE_EFFECT;
			
			messages.add(cause, details);

		} else if (message.is(MultistompCause.PATCH)) {
			Patch patch = (Patch) message.nextMessage().causer();
			Effect effect = (Effect) message.nextMessage().nextMessage().causer();
			int idEffect = patch.effects().indexOf(effect);
			
			details.effect = idEffect;
			details.param = effect.params().indexOf(message.realMessage().causer());
			details.value = ((Param) message.realMessage().causer()).getValue();

			messages.add(CommonCause.SET_PARAM, details);
		}

		return messages;
	}


	/*************************************************/

	public abstract Messages start();
}