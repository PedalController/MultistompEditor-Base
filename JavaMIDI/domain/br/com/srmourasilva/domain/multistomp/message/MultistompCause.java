package br.com.srmourasilva.domain.multistomp.message;

import br.com.srmourasilva.domain.message.Cause;

public enum MultistompCause implements Cause {
	/** Super has the cause */
	SUPER,
	/** None change detected */
	NONE,
	
	MULTISTOMP,
	PATCH,
	EFFECT,
	PARAM,
}
