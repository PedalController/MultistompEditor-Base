package br.com.srmourasilva.architecture;

public interface Observable {
	void addObserver(Observer observer);
	void updateAll(Observable observable);
}