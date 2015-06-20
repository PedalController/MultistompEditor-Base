package br.com.srmourasilva.domain;

import br.com.srmourasilva.domain.message.ChangeMessage;

public interface OnChangeListenner<Type> {
	void onChange(ChangeMessage<Type> message);
}
