package br.com.srmourasilva.multistomp.zoom.gseries.decoder;

import javax.sound.midi.MidiMessage;

import br.com.srmourasilva.domain.message.ChangeMessage;
import br.com.srmourasilva.domain.message.Details;
import br.com.srmourasilva.domain.message.Details.TypeChange;
import br.com.srmourasilva.domain.multistomp.Multistomp;
import br.com.srmourasilva.multistomp.connection.codification.MessageDecoder;
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
	public ChangeMessage<Multistomp> decode(MidiMessage message, Multistomp multistomp) {
		int patch = message.getMessage()[PATCH];

		Details details = new Details(TypeChange.PATCH_NUMBER, patch);

		return ChangeMessage.For(multistomp, details);
	}
}