package br.com.srmourasilva.multistomp.simulator;

import br.com.srmourasilva.domain.OnMultistompListener;
import br.com.srmourasilva.domain.message.Messages;
import br.com.srmourasilva.domain.message.Messages.Message;

public class Log implements OnMultistompListener {
	
	private String type;

	public Log(String type) {
		this.type = type;
	}

	@Override
	public void onChange(Messages messages) {
		for (Message message : messages) {
			System.err.println("LOG:: " + type);
			System.out.println("LOG:: " + message);
		}
	}
}
