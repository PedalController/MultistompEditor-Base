package br.com.srmourasilva.domain.multistomp;

import br.com.srmourasilva.domain.message.Cause;
import br.com.srmourasilva.domain.message.CommonCause;
import br.com.srmourasilva.domain.message.Messages;
import br.com.srmourasilva.domain.message.Messages.Details;
import br.com.srmourasilva.domain.message.Messages.Message;
import br.com.srmourasilva.domain.multistomp.message.ChangeMessage;
import br.com.srmourasilva.domain.multistomp.message.Details.TypeChange;
import br.com.srmourasilva.domain.multistomp.message.MultistompCause;

class MultistompMessagesConverter {

	public static Messages convert(ChangeMessage<Multistomp> message) {
		Message msg = null;

		if (message.is(MultistompCause.MULTISTOMP))			
			msg = convertToPatch(message);
		
		else if (message.is(MultistompCause.PATCH))
			msg = convertPatch(message);

		else if (message.is(MultistompCause.EFFECT))
			msg = convertStatusEffect(message);

		else if (message.is(MultistompCause.PARAM))
			msg = convertSetParam(message);


		if (msg != null)
			return Messages.For(msg);
		else
			return Messages.For();
	}

	private static Message convertToPatch(ChangeMessage<Multistomp> message) {
		Details details = new Details();
		details.patch = message.causer().getIdCurrentPatch();

		return new Message(CommonCause.TO_PATCH, details);
	}
	
	private static Message convertPatch(ChangeMessage<Multistomp> message) {
		if (message.realMessage().details().type() != TypeChange.PATCH_NAME)
			return null;

		Details details = new Details();

		details.value = message.realMessage().details().newValue();

		return new Message(CommonCause.PATCH_NAME, details);
	}

	private static Message convertStatusEffect(ChangeMessage<Multistomp> message) {
		Details details = new Details();

		Patch patch = (Patch) message.nextMessage().causer();
		details.patch = message.causer().patchs().indexOf(patch);

		Effect effect = (Effect) message.realMessage().causer();
		int idEffect = patch.effects().indexOf(effect);
		
		details.effect = idEffect;
		Cause cause = effect.hasActived() ? CommonCause.ACTIVE_EFFECT : CommonCause.DISABLE_EFFECT;
		
		return new Message(cause, details);
	}

	private static Message convertSetParam(ChangeMessage<Multistomp> message) {
		Details details = new Details();

		Patch patch = (Patch) message.nextMessage().causer();
		Effect effect = (Effect) message.nextMessage().nextMessage().causer();
		int idEffect = patch.effects().indexOf(effect);
		
		details.effect = idEffect;
		details.param = effect.params().indexOf(message.realMessage().causer());
		details.value = ((Param) message.realMessage().causer()).getValue();

		return new Message(CommonCause.SET_PARAM, details);
	}
}