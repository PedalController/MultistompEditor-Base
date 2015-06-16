package br.com.srmourasilva.multistomp.zoom.gseries;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiMessage;

import br.com.srmourasilva.domain.message.MessageDecoder;
import br.com.srmourasilva.domain.multistomp.Multistomp;
import br.com.srmourasilva.multistomp.zoom.gseries.decoder.ZoomGSeriesParamDecoder;
import br.com.srmourasilva.multistomp.zoom.gseries.decoder.ZoomGSeriesSelectPatchDecoder;
import br.com.srmourasilva.multistomp.zoom.gseries.decoder.ZoomGSeriesToogleEffectDecoder;

public class ZoomGSeriesMessageDecoder implements MessageDecoder {

	private List<MessageDecoder> decoders;

	public ZoomGSeriesMessageDecoder() {
		decoders = new ArrayList<>();
		decoders.add(new ZoomGSeriesToogleEffectDecoder());
		decoders.add(new ZoomGSeriesSelectPatchDecoder());
		decoders.add(new ZoomGSeriesParamDecoder());
	}
	
	@Override
	public boolean isForThis(MidiMessage message) {
		for (MessageDecoder decoder : decoders)
			if (decoder.isForThis(message)) {
				System.out.println(decoder.getClass().getSimpleName());
				return true;
			}

		return false;
	}

	@Override
	public void decode(MidiMessage message, Multistomp multistomp) {
		for (MessageDecoder decoder : decoders)
			if (decoder.isForThis(message)) {
				//decoder.decode(message, multistomp);
				System.out.println(decoder.getClass().getSimpleName());
			}
	}
}
