package br.com.srmourasilva.domain.multistomp.message;

import br.com.srmourasilva.domain.message.Cause;


public class Details implements Cause {
	public enum TypeChange {
		NONE,
		PEDAL_STATUS,
		PARAM,
		PATCH_NUMBER;
	}

	private TypeChange type;
	private int newValue;

	public static Details NONE() {
		return new Details(TypeChange.NONE, 0);
	}
	
	public Details(TypeChange type, int newValue) {
		this.type = type;
		this.newValue = newValue;
	}

	public int newValue() {
		return newValue;
	}
	
	public TypeChange type() {
		return type;
	}
	
	@Override
	public String toString() {
		return "(" + type + " " + newValue + ")";
	}
}