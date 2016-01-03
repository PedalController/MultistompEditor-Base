package br.com.srmourasilva.domain.multistomp;

import java.util.Optional;

import br.com.srmourasilva.domain.multistomp.message.ChangeMessage;
import br.com.srmourasilva.domain.multistomp.message.Details;
import br.com.srmourasilva.domain.multistomp.message.Details.TypeChange;
import br.com.srmourasilva.domain.multistomp.message.MultistompCause;
import br.com.srmourasilva.domain.multistomp.message.OnChangeListener;

public class Param {

	private String name;

	private int minValue;
	private int maxValue;
	private int currentValue;

	/** Pula de TANTO em TANTO */
	private int stepByStep = 1;

	private Optional<OnChangeListener<Param>> listener = Optional.empty();

	public Param(String name, int minValue, int maxValue, int defaultValue, int stepByStep) {
		this.name = name;

		this.minValue = minValue;
		this.maxValue = maxValue;

		setCurrentValue(defaultValue);
		this.stepByStep = stepByStep;
	}
	
	private void setCurrentValue(int newValue) {
		if (!isValidValue(newValue)) {
			if (newValue > maxValue)
				newValue = maxValue;
			else
				newValue = minValue;
		}

		this.currentValue = newValue;
		
		Details<Integer> details = new Details<>(TypeChange.PARAM, currentValue);

		ChangeMessage<Param> message = new ChangeMessage<>(MultistompCause.PATCH, this, details);
		notify(message);
	}

	private boolean isValidValue(int value) {
		return !(value < minValue || value > maxValue);
	}

	private void notify(ChangeMessage<Param> message) {
		if (!listener.isPresent())
			return;

		listener.get().onChange(message);
	}

	public final String getName() {
		return name;
	}

	public final int getValue() {
		return currentValue;
	}

	public final void setValue(int value) {
		setCurrentValue(value);
	}

	public void addValue() {
		int newValue = currentValue + stepByStep;

		if (!isValidValue(newValue))
			// Don't change current value
			return;

		setValue(newValue);
	}
	
	public final int getMinValue() {
		return minValue;
	}
	public final int getMaxValue() {
		return maxValue;
	}

	/*************************************************/

	public void setListener(OnChangeListener<Param> listener) {
		this.listener = Optional.of(listener);
	}
	
	@Override
	public String toString() {
		return name + "=" + currentValue + "[" + minValue + "-" + maxValue + "]";
	}
}