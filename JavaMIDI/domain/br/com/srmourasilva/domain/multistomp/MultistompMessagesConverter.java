package br.com.srmourasilva.domain.multistomp;

import br.com.srmourasilva.domain.message.Cause;
import br.com.srmourasilva.domain.message.CommonCause;
import br.com.srmourasilva.domain.message.Messages;
import br.com.srmourasilva.domain.message.Messages.Details;
import br.com.srmourasilva.domain.message.Messages.Message;
import br.com.srmourasilva.domain.multistomp.message.ChangeMessage;
import br.com.srmourasilva.domain.multistomp.message.MultistompCause;

class MultistompMessagesConverter {

	public static Messages convert(ChangeMessage<Multistomp> message) {
		Message msg = null;
		br.com.srmourasilva.domain.message.Messages.Details details = new br.com.srmourasilva.domain.message.Messages.Details();

		if (message.is(MultistompCause.MULTISTOMP))			
			msg = convertToPatch(message, details);

		else if (message.is(MultistompCause.EFFECT))
			msg = convertStatusEffect(message, details);

		else if (message.is(MultistompCause.PATCH))
			msg = convertSetParam(message, details);


		if (msg != null)
			return Messages.For(msg);
		else
			return Messages.For();
	}

	private static Message convertToPatch(ChangeMessage<Multistomp> message, Details details) {
		details.patch = message.causer().getIdCurrentPatch();

		return new Message(CommonCause.TO_PATCH, details);
	}

	private static Message convertStatusEffect(ChangeMessage<Multistomp> message, Details details) {
		Patch patch = (Patch) message.nextMessage().causer();
		details.patch = message.causer().patchs().indexOf(patch);

		Effect effect = (Effect) message.realMessage().causer();
		int idEffect = patch.effects().indexOf(effect);
		
		details.effect = idEffect;
		Cause cause = effect.hasActived() ? CommonCause.ACTIVE_EFFECT : CommonCause.DISABLE_EFFECT;
		
		return new Message(cause, details);
	}

	private static Message convertSetParam(ChangeMessage<Multistomp> message, Details details) {
		Patch patch = (Patch) message.nextMessage().causer();
		Effect effect = (Effect) message.nextMessage().nextMessage().causer();
		int idEffect = patch.effects().indexOf(effect);
		
		details.effect = idEffect;
		details.param = effect.params().indexOf(message.realMessage().causer());
		details.value = ((Param) message.realMessage().causer()).getValue();

		return new Message(CommonCause.SET_PARAM, details);
	}
}