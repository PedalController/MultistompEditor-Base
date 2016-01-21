package effect;

import br.com.srmourasilva.pedalboard.Plugabble;

public class Comp implements Plugabble {

	private Plugabble next;

	@Override
	public Plugabble connect(Plugabble plugabble) {
		this.next = plugabble;
		return next;
	}

}
