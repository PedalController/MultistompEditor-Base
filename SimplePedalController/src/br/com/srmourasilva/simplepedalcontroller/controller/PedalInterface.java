package br.com.srmourasilva.simplepedalcontroller.controller;

import br.com.srmourasilva.simplepedalcontroller.domain.Pedalboard;
import br.com.srmourasilva.simplepedalcontroller.domain.Pedalboard.PedalboardListener;
import br.com.srmourasilva.simplepedalcontroller.presenter.Presenter;

public class PedalInterface implements PedalboardListener {
	
	private Pedalboard pedalboard;
	private Presenter presenter;

	public PedalInterface(Pedalboard pedalboard) {
		this.pedalboard = pedalboard;
		this.pedalboard.setListener(this);
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public void setTitle(String string) {
		System.out.println(string);
	}

	public void active(int effect) {
		pedalboard.active(effect);
	}

	public void disable(int effect) {
		pedalboard.disable(effect);
	}

	@Override
	public void onClicked(int idPedal) {
		this.presenter.toogleEffectOf(idPedal);
	}
}
