package br.com.srmourasilva.architecture;

import java.util.ArrayList;
import java.util.List;

/**
 * ObserverObservable basicamente passa a mensagem recebida
 * para os que estão observando-o
 * 
 * Ele serve como ponte de comunicação
 */
public class ObserverObservable implements Observable, Observer {

	private List<Observer> observers = new ArrayList<Observer>();

	/*************************************************/

	@Override
	public final void addObserver(Observer observer) {
		this.observers.add(observer);
	}

	@Override
	public final void updateAll(Observable observable) {
		for (Observer observer : observers) {
			observer.update(observable);
		}
	}


	/*************************************************/
	@Override
	public final void update(Observable observable) {
		this.update(observable);
	}
}
