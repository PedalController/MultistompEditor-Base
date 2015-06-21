package br.com.srmourasilva.multistomp.zoom.gseries.decoder;

import javax.sound.midi.MidiMessage;

import br.com.srmourasilva.domain.message.Messages;
import br.com.srmourasilva.domain.multistomp.Multistomp;

public class ZoomGSeriesPatchDecoder extends AbstractZoomGSeriesPatchDecoder {

	private final int PATCH = 7;

	@Override
	public Messages decode(MidiMessage message, Multistomp multistomp) {
		Messages returned = super.decode(message, multistomp);

		final int patch = message.getMessage()[PATCH];
		returned.forEach(messagem -> messagem.details().patch = patch);

		return returned;
	}

	@Override
	protected int size() {
		return 120;
	}

	@Override
	protected int[] patches() {
		return new int[] {6+5, 19+5, 33+5, 47+5, 60+5, 74+5};
	}

	@Override
	protected boolean refressAll() {
		return true;
	}
}