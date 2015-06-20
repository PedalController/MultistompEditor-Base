package br.com.srmourasilva.multistomp.simulator;

import br.com.srmourasilva.domain.OnChangeListenner;
import br.com.srmourasilva.domain.message.ChangeMessage;
import br.com.srmourasilva.domain.multistomp.Multistomp;

public class Log implements OnChangeListenner<Multistomp> {
	
	private String type;

	public Log(String type) {
		this.type = type;
	}
	
	@Override
	public void onChange(ChangeMessage<Multistomp> message) {
		System.err.println("LOG:: " + type);
		System.out.println("LOG:: " + message);
		System.out.println("LOG:: " + message.realMessage().details());
	}
}
