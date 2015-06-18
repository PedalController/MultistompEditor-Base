package br.com.srmourasilva.decoder.zoom.g3.v2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.srmourasilva.decoder.arquitetura.DecodeFor;
import br.com.srmourasilva.decoder.arquitetura.DecoderSeparator;

// Descobrir bit que informa qual pedal está ligado ou não
@DecodeFor(effect=6, param=0)
public class Effect0Effect1 implements DecoderSeparator {

	@Override
	public Collection<String> mensagens() {
		
		List<String> msgs = new ArrayList<>();

		// 1 - FD-COMBO DESLIGADO + 2 FD-COMBO LIGADO
		// Param tudo zerado
		//msgs.add("f0 52 00 5a 28 40 38 00 00 00 00 00 00 02 00 00 00 00 00 39 00 00 00 00 00 00 00 00 00 08 00 00 00 56 00 00 00 00 00 00 00 00 00 00 00 20 00 56 00 00 00 00 00 01 00 00 00 00 00 00 56 00 00 00 00 00 00 00 00 04 00 00 00 00 56 00 00 00 00 00 00 00 00 00 00 10 00 00 64 00 1c 00 00 20 00 40 58 62 61 61 61 00 61 61 61 61 61 61 00 f7");

		//msgs.add("f0 52 00 5a 28 40 38 00 00 00 00 00 00 02 00 00 00 00 00 39 00 00 00 00 00 00 00 00 00 00 00 00 00 2e 00 00 00 00 00 00 00 00 00 00 00 20 00 56 00 00 00 00 00 01 00 00 00 00 00 00 56 00 00 00 00 00 00 00 00 04 00 00 00 00 56 00 00 00 00 00 00 00 00 00 00 10 00 00 64 00 1c 00 00 20 00 40 58 62 61 61 61 00 61 61 61 61 61 61 00 f7");
		//msgs.add("f0 52 00 5a 28 40 38 00 00 00 00 00 00 02 00 00 28 00 00 39 00 00 00 00 00 00 00 00 00 00 00 00 00 2e 00 00 00 00 00 00 00 00 00 00 00 20 00 56 00 00 00 00 00 01 00 00 00 00 00 00 56 00 00 00 00 00 00 00 00 04 00 00 00 00 56 00 00 00 00 00 00 00 00 00 00 10 00 00 64 00 1c 00 00 20 00 40 58 62 61 61 61 00 61 61 61 61 61 61 00 f7");
		msgs.add("f0 52 00 5a 28 40 38 00 00 00 00 00 00 02 00 00 28 00 00 39 00 00 00 00 00 00 00 00 00 00 00 00 00 2f 00 00 00 00 00 00 00 00 00 00 00 20 00 56 00 00 00 00 00 01 00 00 00 00 00 00 56 00 00 00 00 00 00 00 00 04 00 00 00 00 56 00 00 00 00 00 00 00 00 00 00 10 00 00 64 00 1c 00 00 20 00 40 58 62 61 61 61 00 61 61 61 61 61 61 00 f7");
		
		return msgs;
	}
}