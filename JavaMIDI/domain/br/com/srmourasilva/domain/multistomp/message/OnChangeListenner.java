package br.com.srmourasilva.domain.multistomp.message;

public interface OnChangeListenner<Type> {
	void onChange(ChangeMessage<Type> message);
}