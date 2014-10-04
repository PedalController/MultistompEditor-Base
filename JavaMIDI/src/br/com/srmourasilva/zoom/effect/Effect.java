package br.com.srmourasilva.zoom.effect;

import br.com.srmourasilva.zoom.effect.param.Param;

public interface Effect {
	/** Midi Id for send message */
	int getMidiId();

	/** If this has actived, disable it
	 *  If this has disabled, active it
	 * 
	 * TODO - Descobrir um modo de fazer isActived()
	 */
	//boolean isActived();

	void active();

	void disable();

	String getName();

	boolean getState();

	void setState(boolean state);

	Param getParam(int id);

	/** Set the 'id' param with the new 'value'
	 */
	void setParamValue(int id, int value);
}