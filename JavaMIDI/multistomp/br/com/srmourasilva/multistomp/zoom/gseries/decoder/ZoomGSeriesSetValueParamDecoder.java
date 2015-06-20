package br.com.srmourasilva.multistomp.zoom.gseries.decoder;

import javax.sound.midi.MidiMessage;

import br.com.srmourasilva.domain.message.ChangeMessage;
import br.com.srmourasilva.domain.message.Details;
import br.com.srmourasilva.domain.message.Details.TypeChange;
import br.com.srmourasilva.domain.multistomp.Effect;
import br.com.srmourasilva.domain.multistomp.Multistomp;
import br.com.srmourasilva.domain.multistomp.Param;
import br.com.srmourasilva.domain.multistomp.Patch;

/**
 * f0 52 00 5a 31   05    02   02   00 f7
 * f0 52 00 5a 31 Pedal Param Value 00 f7
 */
public class ZoomGSeriesSetValueParamDecoder extends ZoomGSeriesEffectParamDecoder {
	@Override
	public boolean isForThis(MidiMessage message) {
		return super.isForThis(message) &&
			   message.getMessage()[PARAM] >= 2;
	}

	@Override
	protected ChangeMessage<Multistomp> decode(Multistomp multistomp, int effect, int param, int value) {
		Patch patch = multistomp.currentPatch();
		Effect efeito = patch.effects().get(effect);
		Param parameter = efeito.params().get(param);

		Details details = new Details(TypeChange.PARAM, value);

		return ChangeMessage.For(multistomp, patch, efeito, parameter, details);
	}
}