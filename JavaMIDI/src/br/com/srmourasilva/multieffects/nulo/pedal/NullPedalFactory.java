package br.com.srmourasilva.multieffects.nulo.pedal;

import br.com.srmourasilva.multieffects.Pedal;
import br.com.srmourasilva.multieffects.PedalFactory;
import br.com.srmourasilva.multieffects.PedalType;

public class NullPedalFactory implements PedalFactory {
	public static Pedal getPedal(PedalType type) {
		return new NullPedal();
	}
}
