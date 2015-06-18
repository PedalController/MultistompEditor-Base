package br.com.srmourasilva.decoder.zoom.g3.v2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.srmourasilva.decoder.arquitetura.DecodeFor;
import br.com.srmourasilva.decoder.arquitetura.DecoderSeparator;

@DecodeFor(effect=0, param=2)
public class Effect0Param2 implements DecoderSeparator {

	@Override
	public Collection<String> mensagens() {
		// FD-COMBO
		List<String> msgs = new ArrayList<>();

		// 0
		//msgs.add("f0 52 00 5a 28 40 39 00 00 00 00 00 00 02 00 00 00 00 00 56 00 00 00 00 00 00 00 00 00 08 00 00 00 56 00 00 00 00 00 00 00 00 00 00 00 20 00 56 00 00 00 00 00 01 00 00 00 00 00 00 56 00 00 00 00 00 00 00 0c 04 0c 64 64 00 56 00 00 00 00 00 00 00 00 00 00 00 00 00 64 00 1c 00 00 20 00 40 58 62 61 61 61 00 61 61 61 61 61 61 00 f7"); 
		// 127
		msgs.add("f0 52 00 5a 28 44 39 00 00 00 78 03 00 02 00 00 00 00 00 56 00 00 00 00 00 00 00 00 00 08 00 00 00 56 00 00 00 00 00 00 00 00 00 00 00 20 00 56 00 00 00 00 00 01 00 00 00 00 00 00 56 00 00 00 00 00 00 00 0c 04 0c 64 64 00 56 00 00 00 00 00 00 00 00 00 00 00 00 00 64 00 1c 00 00 20 00 40 58 62 61 61 61 00 61 61 61 61 61 61 00 f7");
		// 143
		msgs.add("f0 52 00 5a 28 40 39 00 00 00 78 04 00 02 00 00 00 00 00 56 00 00 00 00 00 00 00 00 00 08 00 00 00 56 00 00 00 00 00 00 00 00 00 00 00 20 00 56 00 00 00 00 00 01 00 00 00 00 00 00 56 00 00 00 00 00 00 00 0c 04 0c 64 64 00 56 00 00 00 00 00 00 00 00 00 00 00 00 00 64 00 1c 00 00 20 00 40 58 62 61 61 61 00 61 61 61 61 61 61 00 f7");

		return msgs;
	}
}