package br.com.srmourasilva.simplepedalcontroller.domain;

import com.pi4j.component.light.LED;

import br.com.srmourasilva.simplepedalcontroller.domain.clicable.Clicable;

public class PhysicalEffect {

	public static interface OnFootswitchClickListener {
		void onClick(int position);
	}

	private int position;
	private Clicable clicable;
	private LED light;

	/**
	 * @param position 0 is the first
	 * @param momentarySwitch
	 * @param light
	 */
	public PhysicalEffect(int position, Clicable momentarySwitch, LED light) {
		this.position = position;
		this.clicable = momentarySwitch;
		this.light = light;
	}
	
	public void setOnFootswitchClickListener(OnFootswitchClickListener listener) {
		this.clicable.setListener(clicable -> listener.onClick(this.position));
	}
	
	public int getPosition() {
		return position;
	}

	public void activeLed() {
		this.light.on();
	}
	
	public void disableLed() {
		this.light.off();
	}
	
	public void switchLed() {
		this.light.on();
	}
}
