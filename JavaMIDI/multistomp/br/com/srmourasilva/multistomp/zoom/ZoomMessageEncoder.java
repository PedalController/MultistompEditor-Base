package br.com.srmourasilva.multistomp.zoom;

import br.com.srmourasilva.domain.message.ChangeMessage;
import br.com.srmourasilva.domain.message.CommonCause;
import br.com.srmourasilva.domain.message.MessageEncoder;
import br.com.srmourasilva.domain.multistomp.Multistomp;

public abstract class ZoomMessageEncoder implements MessageEncoder {
	protected boolean hasMultistompChange(ChangeMessage<Multistomp> message) {
		return message.cause() == CommonCause.MULTISTOMP;
	}
	
	protected boolean hasPatchChange(ChangeMessage<?> message) {
		return message.cause() == CommonCause.PATCH;
	}
	
	protected boolean hasEffectChange(ChangeMessage<?> message) {
		return message.cause() == CommonCause.EFFECT;
	}
}
