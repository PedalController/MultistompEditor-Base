package br.com.srmourasilva.domain.message;

public enum CommonCause implements Cause {
	/** Super has the cause */
	SUPER,
	
	MULTISTOMP,
	PATCH,
	EFFECT,
	PARAM;
}