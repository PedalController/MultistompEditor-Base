package br.com.srmourasilva.domain.multistomp;

import java.util.Optional;

import br.com.srmourasilva.architecture.exception.ParamException;
import br.com.srmourasilva.domain.OnChangeListenner;
import br.com.srmourasilva.domain.message.ChangeMessage;
import br.com.srmourasilva.domain.message.CommonCause;
import br.com.srmourasilva.domain.message.Details;
import br.com.srmourasilva.domain.message.Details.TypeChange;

public class Param {

	private String name;

	private int minValue;
	private int maxValue;
	private int currentValue;

	/** Pula de TANTO em TANTO */
	private int stepByStep = 1;

	private Optional<OnChangeListenner<Param>> listenner = Optional.empty();

	public Param(String name, int minValue, int maxValue, int defaultValue, int stepByStep) {
		this.name = name;

		this.minValue = minValue;
		this.maxValue = maxValue;

		setCurrentValue(defaultValue);
		this.stepByStep = stepByStep;
	}
	
	private void setCurrentValue(int newValue) {
		if (!isValidValue(newValue))
			throw new ParamException("Invalid new value: " + newValue + ". MinValue: " + minValue + " MaxValue: " + maxValue);

		this.currentValue = newValue;
		
		Details details = new Details(TypeChange.PARAM, currentValue);

		ChangeMessage<Param> message = new ChangeMessage<>(CommonCause.PATCH, this, details);
		notify(message);
	}

	private boolean isValidValue(int value) {
		return !(value < minValue || value > maxValue);
	}

	private void notify(ChangeMessage<Param> message) {
		if (!listenner.isPresent())
			return;

		listenner.get().onChange(message);
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

	public void setOnChangeListenner(OnChangeListenner<Param> listenner) {
		this.listenner = Optional.of(listenner);
	}
	
	@Override
	public String toString() {
		return name + "=" + currentValue + "[" + minValue + "-" + maxValue + "]";
	}
}