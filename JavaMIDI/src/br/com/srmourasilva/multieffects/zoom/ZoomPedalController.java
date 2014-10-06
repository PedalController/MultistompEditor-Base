package br.com.srmourasilva.multieffects.zoom;

import br.com.srmourasilva.multieffects.Pedal;
import br.com.srmourasilva.multieffects.controller.PedalController;

public class ZoomPedalController extends PedalController {
	private ZoomPedal pedal;

	public ZoomPedalController(Pedal pedal) {
		super(pedal);
		this.pedal = (ZoomPedal) pedal;
	}

	@Override
	public void nextPatch() {
		pedal.nextPatch();
	}

	@Override
	public void beforePatch() {
		pedal.beforePatch();
	}

	@Override
	public void setPatch(int id) {
		pedal.setPatch(id);
	}

	@Override
	public void activeEffect(int idEffect) {
		pedal.getCurrentPatch().getEffects().get(idEffect).active();
	}

	@Override
	public void disableEffect(int idEffect) {
		pedal.getCurrentPatch().getEffects().get(idEffect).disable();
	}

	@Override
	public void setEffectParam(int idEffect, int newValue) {
		// FIXME Auto-generated method stub
	}
}