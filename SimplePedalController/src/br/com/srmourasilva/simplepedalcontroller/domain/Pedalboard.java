package br.com.srmourasilva.simplepedalcontroller.domain;

import java.util.ArrayList;
import java.util.List;

import com.pi4j.component.button.Button;
import com.pi4j.component.button.ButtonEvent;
import com.pi4j.component.button.ButtonPressedListener;

public class Pedalboard implements ButtonPressedListener {
	
	public interface PedalboardListener {
		public void onClicked(int idPedal);
	}

	private List<Pedal> pedals;
	private List<PedalboardListener> listeners;

	public Pedalboard() {
		this.pedals = new ArrayList<Pedal>();
		
		this.listeners = new ArrayList<>();
	}

	public void add(Pedal ... pedals) {
		for (Pedal pedal : pedals) {
			this.pedals.add(pedal);
			pedal.addListener(this);
		}
	}

	@Override
	public void onButtonPressed(ButtonEvent event) {
		final int idPedal = getIdPedalBy(event.getButton());
		listeners.forEach(listener -> listener.onClicked(idPedal));
	}

	private int getIdPedalBy(Button button) {
		int idPedal = 0;
		for (Pedal pedal : pedals)
			if (pedal.isThis(button))
				return idPedal;
			else
				idPedal++;

		return -1;
	}
	
	public void add(PedalboardListener ... listeners) {
		for (PedalboardListener listener : listeners)
			this.listeners.add(listener);
	}

	public void active(int effect) {
		pedals.get(effect).active();
	}

	public void disable(int effect) {
		pedals.get(effect).disable();
	}
}