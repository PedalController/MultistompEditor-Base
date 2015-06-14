package br.com.srmourasilva.multistomp.nulo;

import br.com.srmourasilva.domain.multistomp.Multistomp;
import br.com.srmourasilva.domain.multistomp.Patch;
import br.com.srmourasilva.multieffects.PedalType;

public class NullMultistomp extends Multistomp {

	private String MSG_ERROR = "Pedal Unknown is unimplemented";

	public NullMultistomp() {
		this.addPatch(new Patch(0));
		System.out.println(MSG_ERROR);
	}

	@Override
	public void initialize() {
		System.out.println(MSG_ERROR);
	}

	@Override
	public void terminate() {
		System.out.println(MSG_ERROR);
	}

	@Override
	public PedalType getPedalType() {
		return PedalType.Null;
	}
}