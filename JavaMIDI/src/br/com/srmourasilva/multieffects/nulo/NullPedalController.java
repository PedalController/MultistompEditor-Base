package br.com.srmourasilva.multieffects.nulo;

import br.com.srmourasilva.multieffects.Pedal;
import br.com.srmourasilva.multieffects.controller.PedalController;

public final class NullPedalController extends PedalController {

	private String MSG_ERRO = "Pedal Unknown is unimplemented";

	public NullPedalController(Pedal pedal) {
		super(pedal);
		System.out.println(MSG_ERRO);
	}

	@Override
	public void nextPatch() {
		System.out.println(MSG_ERRO);
	}

	@Override
	public void beforePatch() {
		System.out.println(MSG_ERRO);
	}

	@Override
	public void setPatch(int id) {
		System.out.println(MSG_ERRO);
	}

	@Override
	public void activeEffect(int idEffect) {
		System.out.println(MSG_ERRO);
	}

	@Override
	public void disableEffect(int idEffect) {
		System.out.println(MSG_ERRO);
	}

	@Override
	public void setEffectParam(int idEffect, int newValue) {
		System.out.println(MSG_ERRO);
	}
}