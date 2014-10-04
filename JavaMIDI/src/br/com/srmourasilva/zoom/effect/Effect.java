package br.com.srmourasilva.zoom.effect;

public interface Effect {
	/** Midi Id for send message */
	int getMidiId();

	/** If this has actived, disable it
	 *  If this has disabled, active it
	 * 
	 * TODO - Descobrir um modo de fazer isActived()
	 */
	//boolean isActived();

	/**
	 * @return State updated
	 */
	void active();
	void disable();
	void setState(boolean state);
	boolean getState();

	String getName();
}
