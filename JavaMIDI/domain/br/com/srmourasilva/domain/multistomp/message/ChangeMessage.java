package br.com.srmourasilva.domain.multistomp.message;

import br.com.srmourasilva.domain.message.Cause;
import br.com.srmourasilva.domain.multistomp.Effect;
import br.com.srmourasilva.domain.multistomp.Multistomp;
import br.com.srmourasilva.domain.multistomp.Param;
import br.com.srmourasilva.domain.multistomp.Patch;


public class ChangeMessage<Causer> {

	private Causer causer;
	private Cause cause;
	private Details details;	

	private ChangeMessage<?> nextMessage = null;

	public static ChangeMessage<Multistomp> None(Multistomp multistomp) {
		return new ChangeMessage<Multistomp>(MultistompCause.NONE, multistomp, Details.NONE());
	}

	public static ChangeMessage<Multistomp> For(Multistomp multistomp, Details details) {
		return new ChangeMessage<Multistomp>(MultistompCause.MULTISTOMP, multistomp, details);
	}

	public static ChangeMessage<Multistomp> For(Multistomp multistomp, Patch patch, Details details) {
		ChangeMessage<Patch>  patchMsg  = new ChangeMessage<Patch>(MultistompCause.PATCH, patch, details);
		ChangeMessage<Multistomp> multistompMsg = new ChangeMessage<Multistomp>(MultistompCause.SUPER, multistomp, patchMsg);

		return multistompMsg;
	}

	public static ChangeMessage<Multistomp> For(Multistomp multistomp, Patch patch, Effect effect, Details details) {
		ChangeMessage<Effect> effectMsg = new ChangeMessage<Effect>(MultistompCause.EFFECT, effect, details);
		ChangeMessage<Patch>  patchMsg  = new ChangeMessage<Patch>(MultistompCause.SUPER, patch, effectMsg);
		ChangeMessage<Multistomp> multistompMsg = new ChangeMessage<Multistomp>(MultistompCause.SUPER, multistomp, patchMsg);

		return multistompMsg;
	}

	public static ChangeMessage<Multistomp> For(Multistomp multistomp, Patch patch, Effect efeito, Param param, Details details) {
		ChangeMessage<Param>  paramMsg  = new ChangeMessage<Param>(MultistompCause.PARAM, param, details);
		ChangeMessage<Effect> effectMsg = new ChangeMessage<Effect>(MultistompCause.SUPER, efeito, paramMsg);
		ChangeMessage<Patch>  patchMsg  = new ChangeMessage<Patch>(MultistompCause.SUPER, patch, effectMsg);
		ChangeMessage<Multistomp> multistompMsg = new ChangeMessage<Multistomp>(MultistompCause.SUPER, multistomp, patchMsg);

		return multistompMsg;
	}

	public ChangeMessage(Cause cause, Causer causer, ChangeMessage<?> nextMessage) {
		this(cause, causer, Details.NONE());
		this.nextMessage = nextMessage;
	}
	
	public ChangeMessage(Cause cause, Causer causer, Details details) {
		this.cause = cause;
		this.causer = causer;
		this.details = details;
	}

	/** Who shot */
	public Causer causer() {
		return causer;
	}
	
	/** What has changed */
	public Cause cause() {
		return cause;
	}
	
	/** Details of what has changed */
	public Details details() {
		return details;
	}

	public ChangeMessage<?> nextMessage() {
		return nextMessage;
	}

	public boolean is(Cause cause) {
		return cause.equals(realMessage().cause());
	}

	public ChangeMessage<?> realMessage() {
		ChangeMessage<?> message = this;
		
		while (message.cause() == MultistompCause.SUPER)
			message = message.nextMessage();

		return message;
	}

	@Override
	public String toString() {
		String returned = "";

		returned += causer.getClass().getSimpleName();

		if (this.cause == MultistompCause.SUPER)
			returned += " -> " + nextMessage.toString();
		else
			returned += " (" + this.cause.toString() + ")";

		return returned;
	}
}