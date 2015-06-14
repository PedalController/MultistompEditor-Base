package br.com.srmourasilva.multistomp.simulator;

import br.com.srmourasilva.architecture.OnChangeListenner;
import br.com.srmourasilva.domain.message.ChangeMessage;
import br.com.srmourasilva.domain.multistomp.Multistomp;

public class Log implements OnChangeListenner<Multistomp> {
	@Override
	public void onChange(ChangeMessage<Multistomp> message) {
		System.out.println("Ocorreu alguma mudança");
		System.out.println(message);
	}
}
