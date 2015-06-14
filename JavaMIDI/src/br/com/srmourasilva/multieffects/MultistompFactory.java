package br.com.srmourasilva.multieffects;

import br.com.srmourasilva.domain.multistomp.Multistomp;

public interface MultistompFactory {
	Multistomp generate(PedalType type);
}