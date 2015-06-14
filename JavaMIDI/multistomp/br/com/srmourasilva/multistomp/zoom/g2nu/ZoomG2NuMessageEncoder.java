package br.com.srmourasilva.multistomp.zoom.g2nu;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import br.com.srmourasilva.domain.message.ChangeMessage;
import br.com.srmourasilva.domain.multistomp.Effect;
import br.com.srmourasilva.domain.multistomp.Multistomp;
import br.com.srmourasilva.multistomp.zoom.ZoomMessageEncoder;

public class ZoomG2NuMessageEncoder extends ZoomMessageEncoder {
	
	private static final int STATE_ON = 0x7f;
	private static final int STATE_OFF= 0x00;

	private static final int SET_PATH = ShortMessage.PROGRAM_CHANGE;
	private static final int SET_STATE_EFFECT= ShortMessage.CONTROL_CHANGE;

	@Override
	public List<MidiMessage> encode(ChangeMessage<Multistomp> message) {
		try {
			if (hasMultistompChange(message))
				return encodeMultistompChange(message);
	
			if (hasPatchChange(message.nextMessage()))
				return null;
	
			if (hasEffectChange(message.nextMessage().nextMessage()))
				return encodeEffectChange(message);

		} catch (InvalidMidiDataException e) {
			throw new RuntimeException(e);
		}

		return null;
	}

	private List<MidiMessage> encodeMultistompChange(ChangeMessage<Multistomp> message) throws InvalidMidiDataException {
		List<MidiMessage> mensagens = new ArrayList<>();		
		mensagens.add(new ShortMessage(SET_PATH, 0, message.causer().getIdCurrentPatch(), 0));
		
		return mensagens;
	}
	
	private List<MidiMessage> encodeEffectChange(ChangeMessage<Multistomp> message) throws InvalidMidiDataException {
		ChangeMessage<Effect> effectMessage = (ChangeMessage<Effect>) message.nextMessage().nextMessage();

		int index = effectMessage.causer().getMidiId();
		boolean actived = effectMessage.causer().hasActived();

		int byteActived = actived ? STATE_ON : STATE_OFF;

		List<MidiMessage> mensagens = new ArrayList<>();
		mensagens.add(new ShortMessage(SET_STATE_EFFECT, 0, index, byteActived));

		return mensagens;
	}
}
