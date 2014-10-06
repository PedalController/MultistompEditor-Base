package br.com.srmourasilva.multieffects.controller;

import br.com.srmourasilva.multieffects.Pedal;
import br.com.srmourasilva.multieffects.PedalType;
import br.com.srmourasilva.multieffects.midi.MidiSender;
import br.com.srmourasilva.multieffects.nulo.NullPedalController;
import br.com.srmourasilva.multieffects.nulo.pedal.NullPedalFactory;
import br.com.srmourasilva.multieffects.zoom.ZoomPedalController;
import br.com.srmourasilva.multieffects.zoom.pedal.ZoomPedalFactory;

public class PedalControllerFactory {

	public static PedalController getPedal(PedalType pedalType) {
		Pedal pedal;
		PedalController controller = null;
		PedalCompany company = pedalType.getCompany();

		if (company == PedalCompany.ZoomCorp) {
			pedal = ZoomPedalFactory.getPedal(pedalType);
			controller = new ZoomPedalController(pedal);

		} else if (company == PedalCompany.Roland) {
			pedal = NullPedalFactory.getPedal(pedalType);
			controller = new NullPedalController(pedal);

		} else if (company == PedalCompany.Line6) {
			pedal = NullPedalFactory.getPedal(pedalType);
			controller = new NullPedalController(pedal);

		} else {
			pedal = NullPedalFactory.getPedal(pedalType);
			controller = new NullPedalController(pedal);
		}

		return controller;
	}

	/** Search the pedal connected on PC
	 * 
	 * FIXME - Não irá funcionar se o dispositivo não 
	 * tiver a capacidade de enviar sinais midi para o PC
	 */
	public static PedalController searchPedal() {
		PedalController controller;
		for (PedalType type : PedalType.values()) {
			if (MidiSender.findDevices(type).size() != 0) {
				return getPedal(type);
			}
		}

		Pedal pedal = NullPedalFactory.getPedal(PedalType.Null);
		controller = new NullPedalController(pedal);

		return controller;
	}
}
