package br.com.srmourasilva.architecture;

import br.com.srmourasilva.domain.message.ChangeMessage;

public interface OnChangeListenner<Type> {
	void onChange(ChangeMessage<Type> message);
}
