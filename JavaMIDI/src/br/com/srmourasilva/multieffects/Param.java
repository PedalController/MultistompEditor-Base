package br.com.srmourasilva.multieffects;

import java.util.ArrayList;
import java.util.List;

import br.com.srmourasilva.architecture.Observable;
import br.com.srmourasilva.architecture.Observer;

/** Param é observado pelo Effect a qual pertence
 */
public abstract class Param implements Observable {

	private List<Observer> observers = new ArrayList<Observer>();

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

		this.currentValue = defaultValue;
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
		this.updateAll(this);
	}

	public final int getMinValue() {
		return minValue;
	}
	public final int getMaxValue() {
		return maxValue;
	}


	/*************************************************/

	@Override
	public final void addObserver(Observer observer) {
		this.addObserver(observer);
	}

	@Override
	public final void updateAll(Observable observable) {
		for (Observer observer : observers) {
			observer.update(observable);
		}
	}
}