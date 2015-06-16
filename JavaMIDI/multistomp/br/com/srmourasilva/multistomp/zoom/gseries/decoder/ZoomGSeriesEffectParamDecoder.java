package br.com.srmourasilva.multistomp.zoom.gseries.decoder;

import javax.sound.midi.MidiMessage;

import br.com.srmourasilva.domain.message.MessageDecoder;
import br.com.srmourasilva.domain.multistomp.Multistomp;
import br.com.srmourasilva.util.MidiMessageTester;

/**
 * f0 52 00 5a 31   05    02   02   00 f7
 * f0 52 00 5a 31 Pedal Param Value 00 f7
 */
public abstract class ZoomGSeriesEffectParamDecoder implements MessageDecoder {
	protected static final int EFFECT = 5;
	protected static final int PARAM = 6;
	protected static final int VALUE = 7;

	@Override
	public boolean isForThis(MidiMessage message) {
		final byte[] begin = new byte[] {
			(byte) 0xf0, 0x52, 0x00, 0x5a
		};
		
		final byte[] end = new byte[] {
			0x00, (byte) 0xf7
		};

		MidiMessageTester tester = new MidiMessageTester(message);
		
		return tester.init()
					 .sizeIs(10)
				     .startingWith(begin)
				     .endingWith(end)
				     .test();
	}

	@Override
	public void decode(MidiMessage message, Multistomp multistomp) {
		int effect = message.getMessage()[EFFECT];
		int param = message.getMessage()[PARAM];
		int value = message.getMessage()[VALUE];

		multistomp.currentPatch().effects().get(effect).params().get(param).setValue(value);
	}
	
	protected abstract void decode(Multistomp multistomp, int effect, int param, int value);
}