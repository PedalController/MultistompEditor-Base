package br.com.srmourasilva.domain.message;

public class ChangeMessage<Causer> {
	private Cause cause;
	private Causer causer;
	private ChangeMessage<?> nextMessage = null;

	public ChangeMessage(Cause cause, Causer causer, ChangeMessage<?> nextMessage) {
		this(cause, causer);
		this.nextMessage = nextMessage;
	}
	
	public ChangeMessage(Cause cause, Causer causer) {
		this.cause = cause;
		this.causer = causer;
	}

	/** What has changed */
	public Cause cause() {
		return cause;
	}

	/** Who shot */
	public Causer causer() {
		return causer;
	}

	public ChangeMessage<?> nextMessage() {
		return nextMessage;
	}
	
	@Override
	public String toString() {
		String returned = "";

		returned += causer.getClass().getSimpleName();

		if (this.cause == CommonCause.SUPER)
			returned += " -> " + nextMessage.toString();
		else
			returned += " (" + this.cause.toString() + ")";

		return returned;
	}
}