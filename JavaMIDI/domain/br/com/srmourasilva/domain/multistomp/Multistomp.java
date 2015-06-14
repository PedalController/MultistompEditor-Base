package br.com.srmourasilva.domain.multistomp;

import java.util.ArrayList;
import java.util.List;

import br.com.srmourasilva.architecture.OnChangeListenner;
import br.com.srmourasilva.architecture.exception.ImplemetationException;
import br.com.srmourasilva.domain.message.ChangeMessage;
import br.com.srmourasilva.domain.message.CommonCause;
import br.com.srmourasilva.multieffects.PedalType;

public abstract class Multistomp implements OnChangeListenner<Patch> {

	private List<OnChangeListenner<Multistomp>> listenners = new ArrayList<>();

	private List<Patch> patchs = new ArrayList<Patch>();

	private int idCurrentPatch = 0;

	/*************************************************/

	/** Inicializate Pedal */
	public abstract void initialize();

	/** Stop the Pedal */
	public abstract void terminate();


	/*************************************************/

	@Deprecated
	public abstract PedalType getPedalType();


	/*************************************************/

	protected void addPatch(Patch patch) {
		patchs.add(patch);
		patch.setOnChangeListenner(this);
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

	public final void nextPatch() {
		this.toPatch(idCurrentPatch+1);
	}

	public final void beforePatch() {
		this.toPatch(idCurrentPatch-1);
	}

	public final void toPatch(int index) {
		if (index >= patchs.size())
			index = 0;

		else if (index < 0)
			index = patchs.size()-1;

		idCurrentPatch = index;

		ChangeMessage<Multistomp> newMessage = new ChangeMessage<>(CommonCause.MULTISTOMP, this);
		this.notify(newMessage);
	}

	/*************************************************/

	@Override
	public final String toString() {
		StringBuffer retorno = new StringBuffer();
		retorno.append("Pedaleira: "   + this.getClass().getSimpleName() + "\n");
		retorno.append(" - Current Patch: " + this.currentPatch().toString() + "\n");
		retorno.append(" - Effects: \n");
		for (Effect effect : this.currentPatch().effects()) {
			retorno.append("  |- " + effect.toString() + "\n");
		}

		return retorno.toString();
	}


	/*************************************************/

	public void addListenner(OnChangeListenner<Multistomp> listenner) {
		this.listenners.add(listenner);
	}

	@Override
	public void onChange(ChangeMessage<Patch> message) {
		ChangeMessage<Multistomp> newMessage = new ChangeMessage<>(CommonCause.SUPER, this, message);
		notify(newMessage);
	}

	private void notify(ChangeMessage<Multistomp> message) {
		for (OnChangeListenner<Multistomp> listenner : listenners)
			listenner.onChange(message);
	}
}