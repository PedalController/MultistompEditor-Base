package br.com.srmourasilva.domain;

import br.com.srmourasilva.domain.message.Messages;

public interface OnMultistompListenner {
	void onChange(Messages messages);
}
