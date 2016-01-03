package br.com.srmourasilva.domain.multistomp.message;

public interface OnChangeListener<Type> {
	void onChange(ChangeMessage<Type> message);
}