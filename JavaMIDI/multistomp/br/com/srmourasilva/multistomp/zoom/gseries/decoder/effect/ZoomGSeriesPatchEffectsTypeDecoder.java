package br.com.srmourasilva.multistomp.zoom.gseries.decoder.effect;

import javax.sound.midi.MidiMessage;

import br.com.srmourasilva.domain.message.Messages;
import br.com.srmourasilva.domain.multistomp.Multistomp;
import br.com.srmourasilva.multistomp.connection.codification.MessageDecoder;
import br.com.srmourasilva.multistomp.zoom.gseries.ZoomG3v2Pedals;

public class ZoomGSeriesPatchEffectsTypeDecoder implements MessageDecoder {

	private static final int[] PATCHES = new int[] {6+5, 19+5, 33+5, 47+5, 60+5, 74+5};
	private static final byte[] PATCHES_MASK = new byte[] {0b01000000, 0b00000010, 0b00001000, 0b00100000, 0b0000001, 0b00000100};
	private static final int[] DECAY = new int[] {1, 6, 4, 2, 7, 5};

	@Override
	public boolean isForThis(MidiMessage message) {
		return true;
	}

	@Override
	public Messages decode(MidiMessage message, Multistomp multistomp) {
		byte[] data = message.getMessage();
		
		System.out.println("Pedals type:");
		System.out.println(" 1º :" + ZoomG3v2Pedals.instance.getEffect(pedal(0, data)));
		System.out.println(" 2º :" + ZoomG3v2Pedals.instance.getEffect(pedal(1, data)));
		System.out.println(" 3º :" + ZoomG3v2Pedals.instance.getEffect(pedal(2, data)));
		System.out.println(" 4º :" + ZoomG3v2Pedals.instance.getEffect(pedal(3, data)));
		System.out.println(" 5º :" + ZoomG3v2Pedals.instance.getEffect(pedal(4, data)));
		System.out.println(" 6º :" + ZoomG3v2Pedals.instance.getEffect(pedal(5, data)));

		return Messages.Empty();
	}
	
	private int pedal(int index, byte[] message) {
		final byte MASK = PATCHES_MASK[index];
		final boolean SIXTY_FOUR = (message[PATCHES[index] - DECAY[index]] & MASK) == MASK;

		int value = SIXTY_FOUR ? 64 : 0;
		value += message[PATCHES[index]] >> 1;

		return value;
	}
}
