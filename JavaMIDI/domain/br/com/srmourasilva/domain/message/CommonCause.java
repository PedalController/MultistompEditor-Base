package br.com.srmourasilva.domain.message;

public enum CommonCause implements Cause {
	/** Super has the cause */
	SUPER,
	/** None change detected */
	NONE,
	
	MULTISTOMP,
	PATCH,
	EFFECT,
	PARAM;
}