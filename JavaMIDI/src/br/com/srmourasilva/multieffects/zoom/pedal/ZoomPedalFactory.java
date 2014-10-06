package br.com.srmourasilva.multieffects.zoom.pedal;

import br.com.srmourasilva.multieffects.Pedal;
import br.com.srmourasilva.multieffects.PedalFactory;
import br.com.srmourasilva.multieffects.PedalType;

public class ZoomPedalFactory implements PedalFactory {

	public static Pedal getPedal(PedalType type) {
		Pedal pedal;

		if (type == PedalType.G2Nu) {
			pedal = new ZoomG2Nu();

		} else if (type == PedalType.G3) {
			pedal = new ZoomGSeries(100, 6);
		} else {
			pedal = new ZoomGSeries(0, 0);
		}

		return pedal;
	}
}