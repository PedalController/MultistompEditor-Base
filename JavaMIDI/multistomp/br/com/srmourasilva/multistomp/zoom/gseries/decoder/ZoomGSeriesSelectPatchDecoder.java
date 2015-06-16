package br.com.srmourasilva.multistomp.zoom.gseries.decoder;

import javax.sound.midi.MidiMessage;

import br.com.srmourasilva.domain.message.MessageDecoder;
import br.com.srmourasilva.domain.multistomp.Multistomp;
import br.com.srmourasilva.util.MidiMessageTester;

/**
 * c0 PATCH
 */
public class ZoomGSeriesSelectPatchDecoder implements MessageDecoder {

	private static final int PATCH = 1;

	@Override
	public boolean isForThis(MidiMessage message) {
		final byte[] begin = new byte[] {(byte) 0xc0};

		MidiMessageTester tester = new MidiMessageTester(message);
		
		return tester.init()
					 .sizeIs(2)
				     .startingWith(begin)
				     .test();
	}

	@Override
	public void decode(MidiMessage message, Multistomp multistomp) {
		int patch = message.getMessage()[PATCH];
		
		multistomp.toPatch(patch);
	}
}