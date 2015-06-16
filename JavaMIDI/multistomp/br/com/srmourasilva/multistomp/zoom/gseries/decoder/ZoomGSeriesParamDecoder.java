package br.com.srmourasilva.multistomp.zoom.gseries.decoder;

import javax.sound.midi.MidiMessage;

import br.com.srmourasilva.domain.multistomp.Multistomp;

/**
 * f0 52 00 5a 31   05    02   02   00 f7
 * f0 52 00 5a 31 Pedal Param Value 00 f7
 */
public class ZoomGSeriesParamDecoder extends ZoomGSeriesEffectParamDecoder {
	@Override
	public boolean isForThis(MidiMessage message) {
		return super.isForThis(message) &&
			   message.getMessage()[PARAM] >= 2;
	}

	@Override
	protected void decode(Multistomp multistomp, int effect, int param, int value) {
		multistomp.currentPatch().effects().get(effect).params().get(param).setValue(value);
	}

}