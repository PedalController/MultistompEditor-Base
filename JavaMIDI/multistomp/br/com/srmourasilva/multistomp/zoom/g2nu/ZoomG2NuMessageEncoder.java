package br.com.srmourasilva.multistomp.zoom.g2nu;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import br.com.srmourasilva.domain.message.Cause;
import br.com.srmourasilva.domain.message.CommonCause;
import br.com.srmourasilva.domain.message.Messages;
import br.com.srmourasilva.domain.message.Messages.Message;
import br.com.srmourasilva.multistomp.connection.codification.MessageEncoder;

public class ZoomG2NuMessageEncoder implements MessageEncoder {
	
	private static final int STATE_ON = 0x7f;
	private static final int STATE_OFF= 0x00;

	private static final int SET_PATH = ShortMessage.PROGRAM_CHANGE;
	private static final int SET_STATE_EFFECT= ShortMessage.CONTROL_CHANGE;

	@Override
	public List<MidiMessage> encode(Messages messages) {
		List<MidiMessage> retorno = new ArrayList<>();

		messages.get(CommonCause.TO_PATCH).forEach(message -> retorno.add(encodeMultistompChange(message)));

		messages.get(CommonCause.ACTIVE_EFFECT).forEach(message -> retorno.add(encodeEffectChange(message, CommonCause.ACTIVE_EFFECT)));
		messages.get(CommonCause.DISABLE_EFFECT).forEach(message -> retorno.add(encodeEffectChange(message, CommonCause.DISABLE_EFFECT)));

		return retorno;
	}

	private MidiMessage encodeMultistompChange(Message message) {
		int patch = message.details().patch;

		try {
			return new ShortMessage(SET_PATH, 0, patch, 0);
		} catch (InvalidMidiDataException e) {
			throw new RuntimeException(e);
		}
	}
	
	private MidiMessage encodeEffectChange(Message message, Cause cause) {
		int effect = message.details().effect;

		boolean actived = cause == CommonCause.ACTIVE_EFFECT;

		int byteActived = actived ? STATE_ON : STATE_OFF;

		try {
			return new ShortMessage(SET_STATE_EFFECT, 0, effect, byteActived);
		} catch (InvalidMidiDataException e) {
			throw new RuntimeException(e);
		}
	}
}
