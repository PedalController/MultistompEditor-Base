package br.com.srmourasilva.multistomp.controller;

import br.com.srmourasilva.domain.message.ChangeMessage;
import br.com.srmourasilva.domain.message.CommonCause;
import br.com.srmourasilva.domain.message.Details;
import br.com.srmourasilva.domain.multistomp.Effect;
import br.com.srmourasilva.domain.multistomp.Multistomp;
import br.com.srmourasilva.domain.multistomp.Param;

// FIXME - Fazer SimpleInterface utilizar também um genérico deste
class MultistompChanger {

	private PedalController controller;

	public MultistompChanger(PedalController controller) {
		this.controller = controller;
	}

	public void attempt(ChangeMessage<Multistomp> message) {
		ChangeMessage<?> real = message.realMessage();

		if (message.is(CommonCause.NONE))
			return;

		else if (message.is(CommonCause.MULTISTOMP))
			update((Multistomp) real.causer(), real.details());
		else if (message.is(CommonCause.PATCH))
			throw new RuntimeException();
		else if (message.is(CommonCause.EFFECT))
			update(message, (Effect) real.causer(), real.details());
		else if (message.is(CommonCause.PARAM))
			update(message, (Param) real.causer(), real.details());
	}

	private void update(Multistomp multistomp, Details details) {
		if (details.type() == Details.TypeChange.PATCH_NUMBER)
			controller.toPatch(details.newValue());
	}

	private void update(ChangeMessage<Multistomp> message, Effect effect, Details details) {
		int idEffect = message.causer().currentPatch().effects().indexOf(effect);

		if (details.type() == Details.TypeChange.PEDAL_STATUS)
			controller.toogleEffect(idEffect);
	}

	private void update(ChangeMessage<Multistomp> message, Param param, Details details) {
		Effect effect = (Effect) message.nextMessage().nextMessage().causer();

		int idEffect = message.causer().currentPatch().effects().indexOf(effect);
		int idParam = effect.params().indexOf(param);

		if (details.type() == Details.TypeChange.PARAM)
			controller.setEffectParam(idEffect, idParam, details.newValue());
	}
}