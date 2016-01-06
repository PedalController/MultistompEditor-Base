package br.com.srmourasilva.multistomp.zoom.gseries.decoder;

import javax.sound.midi.MidiMessage;

import br.com.srmourasilva.domain.message.CommonCause;
import br.com.srmourasilva.domain.message.Messages;
import br.com.srmourasilva.domain.message.Messages.Details;
import br.com.srmourasilva.domain.message.Messages.Message;

public class ZoomGSeriesDisableEffectDecoder extends AbstractZoomGSeriesEffectParamDecoder {

	@Override
	public boolean isForThis(MidiMessage message) {
		return super.isForThis(message) && 
			   message.getMessage()[PARAM] == 0;
	}

	@Override
	protected Messages decode(Details details) {
		details.param = Details.NULL;

		return Messages.For(new Message(CommonCause.EFFECT_DISABLE, details));
	}
}