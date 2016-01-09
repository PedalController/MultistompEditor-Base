package br.com.srmourasilva.simplepedalcontroller.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.pi4j.component.button.Button;
import com.pi4j.component.button.ButtonEvent;
import com.pi4j.component.button.ButtonPressedListener;

public class Pedalboard implements ButtonPressedListener {
	
	public interface PedalboardListener {
		public void onClicked(int idPedal);
	}

	private List<Pedal> pedals;
	private Optional<PedalboardListener> listener;

	public Pedalboard() {
		this.pedals = new ArrayList<Pedal>();
		
		this.listener = Optional.empty();
	}

	public void add(Pedal ... pedals) {
		for (Pedal pedal : pedals) {
			this.pedals.add(pedal);
			pedal.addListener(this);
		}
	}

	@Override
	public void onButtonPressed(ButtonEvent event) {
		if (listener.isPresent()) {
			final int idPedal = getIdPedalBy(event.getButton());
			listener.get().onClicked(idPedal);
		}
	}

	private int getIdPedalBy(Button button) {
		for (Pedal pedal : pedals)
			if (pedal.isThis(button))
				return pedal.getId();

		return -1;
	}
	
	public void setListener(PedalboardListener listener) {
		this.listener = Optional.of(listener);
	}

	public void active(int effect) {
		pedals.get(effect).active();
	}

	public void disable(int effect) {
		pedals.get(effect).disable();
	}
}