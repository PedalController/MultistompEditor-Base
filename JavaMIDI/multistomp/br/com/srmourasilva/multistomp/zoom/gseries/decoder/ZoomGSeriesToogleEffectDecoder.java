package br.com.srmourasilva.multistomp.zoom.gseries.decoder;

import javax.sound.midi.MidiMessage;

import br.com.srmourasilva.domain.multistomp.Effect;
import br.com.srmourasilva.domain.multistomp.Multistomp;

public class ZoomGSeriesToogleEffectDecoder extends ZoomGSeriesEffectParamDecoder {

	@Override
	public boolean isForThis(MidiMessage message) {
		return super.isForThis(message) && 
			   message.getMessage()[PARAM] == 0;
	}

	@Override
	protected void decode(Multistomp multistomp, int effect, int param, int value) {
		Effect efeito = multistomp.currentPatch().effects().get(effect);

		if (value == 1)
			efeito.active();
		else
			efeito.disable();
	}
}