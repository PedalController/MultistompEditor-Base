package br.com.srmourasilva.zoom.effect.param;

public abstract class Param {

	private String name;

	private int minValue;
	private int maxValue;
	private int currentValue;

	/** Pula de TANTO em TANTO */
	private int stepByStep = 1;

	public Param(String name, int minValue, int maxValue, int defaultValue, int stepByStep) {
		this.name = name;

		this.minValue = minValue;
		this.maxValue = maxValue;

		this.currentValue = minValue;
		this.stepByStep = 1;
	}

	public final String getName() {
		return name;
	}

	public final int getValue() {
		return currentValue;
	}
	public final void setValue(int value) {
		this.currentValue += value - value%stepByStep;
	}


	public final int getMinValue() {
		return minValue;
	}
	public final int getMaxValue() {
		return maxValue;
	}
}