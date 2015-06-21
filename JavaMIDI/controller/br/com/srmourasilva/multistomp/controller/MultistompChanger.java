package br.com.srmourasilva.multistomp.controller;

import br.com.srmourasilva.domain.message.CommonCause;
import br.com.srmourasilva.domain.message.Messages.Details;
import br.com.srmourasilva.domain.message.Messages.Message;

// FIXME - Fazer SimpleInterface utilizar também um genérico deste
class MultistompChanger {

	private PedalController controller;

	public MultistompChanger(PedalController controller) {
		this.controller = controller;
	}

	public void attempt(Message message) {
		if (message.is(CommonCause.TO_PATCH))
			controller.toPatch(message.details().patch);

		else if (message.is(CommonCause.ACTIVE_EFFECT) && message.details().patch == Details.NULL)
			controller.activeEffect(message.details().effect);
		
		else if (message.is(CommonCause.ACTIVE_EFFECT) && message.details().patch != Details.NULL)
			controller.multistomp().patchs().get(message.details().patch).effects().get(message.details().effect).active();

		else if (message.is(CommonCause.DISABLE_EFFECT) && message.details().patch == Details.NULL)
			controller.disableEffect(message.details().effect);
		
		else if (message.is(CommonCause.DISABLE_EFFECT) && message.details().patch != Details.NULL)
			controller.multistomp().patchs().get(message.details().patch).effects().get(message.details().effect).disable();

		else if (message.is(CommonCause.SET_PARAM)) {
			int idEffect = message.details().effect;
			int idParam  = message.details().param;
			int newValue = message.details().value;

			controller.setEffectParam(idEffect, idParam, newValue);
		}
	}
}