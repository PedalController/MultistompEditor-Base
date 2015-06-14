package br.com.srmourasilva.multistomp.controller;

import br.com.srmourasilva.domain.multistomp.Multistomp;
import br.com.srmourasilva.multieffects.PedalType;
import br.com.srmourasilva.multieffects.midi.MidiSender;
import br.com.srmourasilva.multistomp.nulo.NullMultistomp;
import br.com.srmourasilva.multistomp.zoom.ZoomMultistompFactory;

public class PedalControllerFactory {
	
	/** 
	 * Search the pedal connected on PC
	 */
	public static PedalController searchPedal() {
		for (PedalType multistomp : PedalType.values())
			if (isConnected(multistomp))
				return getPedal(multistomp);

		return new PedalController(new NullMultistomp());
	}
	
	private static boolean isConnected(PedalType multistomp) {
		return MidiSender.findDevices(multistomp).size() != 0;
	}

	public static PedalController getPedal(PedalType pedalType) {
		Multistomp pedal;
		PedalCompany company = pedalType.getCompany();

		if (company == PedalCompany.ZoomCorp)
			pedal = new ZoomMultistompFactory().generate(pedalType);

		else if (company == PedalCompany.Roland)
			pedal = new NullMultistomp();

		else if (company == PedalCompany.Line6)
			pedal = new NullMultistomp();

		else
			pedal = new NullMultistomp();

		return new PedalController(pedal);
	}
}
