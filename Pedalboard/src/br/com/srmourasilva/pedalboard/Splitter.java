package br.com.srmourasilva.pedalboard;

public class Splitter implements Plugabble {

	public Splitter split(Plugabble ...plugabbles) {
		return null;
	}

	@Override
	public Plugabble connect(Plugabble plugabble) {
		return this;
	}

	public Plugabble join() {
		return null;
	}
}
