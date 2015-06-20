package br.com.srmourasilva.multistomp.zoom.gseries.decoder;

import javax.sound.midi.MidiMessage;

import br.com.srmourasilva.domain.message.ChangeMessage;
import br.com.srmourasilva.domain.message.Details;
import br.com.srmourasilva.domain.message.Details.TypeChange;
import br.com.srmourasilva.domain.multistomp.Effect;
import br.com.srmourasilva.domain.multistomp.Multistomp;
import br.com.srmourasilva.domain.multistomp.Patch;

public class ZoomGSeriesDisableEffectDecoder extends ZoomGSeriesEffectParamDecoder {

	@Override
	public boolean isForThis(MidiMessage message) {
		return super.isForThis(message) && 
			   message.getMessage()[PARAM] == 0;
	}

	@Override
	protected ChangeMessage<Multistomp> decode(Multistomp multistomp, int effect, int param, int value) {
		Patch patch = multistomp.currentPatch();
		Effect efeito = multistomp.currentPatch().effects().get(effect);

		Details details = new Details(TypeChange.PEDAL_STATUS, 0);
	
		return ChangeMessage.For(multistomp, patch, efeito, details);
	}
}