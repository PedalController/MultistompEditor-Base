package br.com.srmourasilva.domain.message;


public enum CommonCause implements Cause {

	// Multistomp
	TO_PATCH,
	GENERAL_VOLUME,

	// Patch
	PATCH_NUMBER,
	PATCH_VOLUME,
	PATCH_NAME,

	// Effect
	ACTIVE_EFFECT,
	DISABLE_EFFECT,

	// Param
	SET_PARAM;
}