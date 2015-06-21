package br.com.srmourasilva.multistomp.zoom.gseries;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

import br.com.srmourasilva.domain.message.CommonCause;
import br.com.srmourasilva.domain.message.Messages;
import br.com.srmourasilva.domain.message.Messages.Message;
import br.com.srmourasilva.multistomp.connection.codification.MessageEncoder;


public class ZoomGSeriesMessageEncoder implements MessageEncoder {

	@Override
	public List<MidiMessage> encode(Messages messages) {
		List<MidiMessage> retorno = new ArrayList<>();

		messages.get(CommonCause.TO_PATCH).forEach(message -> retorno.add(encodeCurrentPatch(message)));

		messages.get(CommonCause.ACTIVE_EFFECT).forEach(message -> retorno.addAll(encodeStatusEffect(message, CommonCause.ACTIVE_EFFECT)));
		messages.get(CommonCause.DISABLE_EFFECT).forEach(message -> retorno.addAll(encodeStatusEffect(message, CommonCause.DISABLE_EFFECT)));

		messages.get(ZoomGSeriesCause.REQUEST_CURRENT_PATCH_NUMBER).forEach(message -> retorno.add(requestCurrentPatch(message)));
		messages.get(ZoomGSeriesCause.REQUEST_SPECIFIC_PATCH_DETAILS).forEach(message -> retorno.add(requestPatchDetails(message)));

		return retorno;
	}

	private MidiMessage encodeCurrentPatch(Message message) {
		final int SET_PATH = ShortMessage.PROGRAM_CHANGE;
		
		int patch = message.details().patch;

		try {
			return new ShortMessage(SET_PATH, 0, patch, 0);
		} catch (InvalidMidiDataException e) {
			throw new RuntimeException(e);
		}
	}

	private List<MidiMessage> encodeStatusEffect(Message message, CommonCause cause) {
		int effect = message.details().effect;

		boolean actived = cause == CommonCause.ACTIVE_EFFECT;

		int byteActived = actived ? 0x01 : 0x00;

		return group(myTurn(), effect(effect, byteActived));
	}
	
	private MidiMessage myTurn() {
		final byte[] MY_TURN = {
			(byte) 0xF0, 0x52, 0x00,
			0x5A, 0x50, (byte) 0xF7
		};

		return customMessageFor(MY_TURN);
	}
	
	private MidiMessage effect(int idParam, int newValue) {
		byte[] setState = {
			(byte) 0xF0, 0x52, 0x00,
			0x5A, 0x31, (byte) idParam,
			0x00, (byte) newValue, 0x00,
			(byte) 0xF7
		};
		
		return customMessageFor(setState);
	}

	private MidiMessage requestCurrentPatch(Message message) {
		byte[] NUMBER_CURRENT_PATCH = {
			(byte) 0xF0, 0x52, 0x00,
			0x5A, 0x33, (byte) 0xF7
		};

		return customMessageFor(NUMBER_CURRENT_PATCH);
	}

	private MidiMessage requestPatchDetails(Message message) {
		int patch = message.details().patch;

		byte[] CURRENT_PATCH = {
			(byte) 0xF0, 0x52, 0x00,
			0x5A, 0x09, (byte) 0x00,
			0x00, (byte) patch, (byte) 0xF7
		};

		return customMessageFor(CURRENT_PATCH);
	}

	private List<MidiMessage> group(MidiMessage ... messages) {
		List<MidiMessage> mensagens = new ArrayList<>();
		
		for (MidiMessage midiMessage : messages)
			mensagens.add(midiMessage);

		return mensagens;
	}

	private SysexMessage customMessageFor(byte[] message) {
		try {
			return new SysexMessage(message, message.length);
		} catch (InvalidMidiDataException e) {
			throw new RuntimeException(e);
		}
	}
}
