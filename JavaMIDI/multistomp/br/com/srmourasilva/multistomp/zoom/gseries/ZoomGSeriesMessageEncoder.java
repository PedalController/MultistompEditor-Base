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

		messages.get(CommonCause.TO_PATCH).forEach(message -> retorno.add(toPatch(message)));

		messages.get(CommonCause.ACTIVE_EFFECT).forEach(message -> retorno.addAll(statusEffect(message, CommonCause.ACTIVE_EFFECT)));
		messages.get(CommonCause.DISABLE_EFFECT).forEach(message -> retorno.addAll(statusEffect(message, CommonCause.DISABLE_EFFECT)));

		messages.get(CommonCause.SET_PARAM).forEach(message -> retorno.add(setParam(message)));

		messages.get(ZoomGSeriesCause.SET_EFFECT).forEach(message -> retorno.add(setEffect(message)));
		
		messages.get(ZoomGSeriesCause.REQUEST_CURRENT_PATCH_NUMBER).forEach(message -> retorno.add(requestCurrentPatchNumber(message)));
		messages.get(ZoomGSeriesCause.REQUEST_CURRENT_PATCH_DETAILS).forEach(message -> retorno.add(requestCurrentPatchDetails(message)));
		messages.get(ZoomGSeriesCause.REQUEST_SPECIFIC_PATCH_DETAILS).forEach(message -> retorno.add(requestSpecificPatchDetails(message)));

		messages.get(ZoomGSeriesCause.LISSEN_ME).forEach(message -> retorno.add(lissenMe()));
		messages.get(ZoomGSeriesCause.YOU_CAN_TALK).forEach(message -> retorno.add(youCanTalk()));

		return retorno;
	}

	private MidiMessage toPatch(Message message) {
		final int SET_PATH = ShortMessage.PROGRAM_CHANGE;
		
		int patch = message.details().patch;

		try {
			return new ShortMessage(SET_PATH, 0, patch, 0);
		} catch (InvalidMidiDataException e) {
			throw new RuntimeException(e);
		}
	}

	private List<MidiMessage> statusEffect(Message message, CommonCause cause) {
		int effect = message.details().effect;

		boolean actived = cause == CommonCause.ACTIVE_EFFECT;
		int byteActived = actived ? 0x01 : 0x00;

		return group(lissenMe(), manupuleEffect(effect, SET_STATUS, byteActived));
	}

	private MidiMessage setParam(Message message) {
		int effect = message.details().effect;
		int param  = message.details().param;
		int value  = message.details().value;

		return manupuleEffect(effect, param + PARAM_EFFECT, value);
	}

	private MidiMessage setEffect(Message message) {
		int effect = message.details().effect;
		int value  = message.details().value;

		return manupuleEffect(effect, CHANGE_EFFECT, value);
	}

	private final int SET_STATUS = 0;
	private final int CHANGE_EFFECT = 1;
	private final int PARAM_EFFECT = 2; // Base

	private MidiMessage manupuleEffect(int effect, int type, int value) {
		int value2 = value / 128;
		value = value % 128;

		return customMessageFor(new byte[] {
			(byte) 0xF0, (byte)  0x52, (byte)   0x00,
			(byte) 0x5A, (byte)  0x31, (byte) effect,
			(byte) type, (byte) value, (byte) value2,
			(byte) 0xF7
		});
	}


	///////////////////////////////////////
	// SPECIFIC ZOOM
	///////////////////////////////////////

	private MidiMessage requestCurrentPatchNumber(Message message) {
		return customMessageFor(new byte[] {
			(byte) 0xF0, (byte) 0x52, (byte) 0x00,
			(byte) 0x5A, (byte) 0x33, (byte) 0xF7
		});
	}

	private MidiMessage requestCurrentPatchDetails(Message message) {
		return customMessageFor(new byte[] {
			(byte) 0xF0, (byte) 0x52, (byte) 0x00,
			(byte) 0x5A, (byte) 0x29, (byte) 0xF7
		});
	}

	private MidiMessage requestSpecificPatchDetails(Message message) {
		int patch = message.details().patch;

		byte[] CURRENT_PATCH = {
			(byte) 0xF0, (byte)  0x52, (byte) 0x00,
			(byte) 0x5A, (byte)  0x09, (byte) 0x00,
			(byte) 0x00, (byte) patch, (byte) 0xF7
		};

		return customMessageFor(CURRENT_PATCH);
	}

	private MidiMessage lissenMe() {
		return customMessageFor(new byte[] {
			(byte) 0xF0, (byte) 0x52, (byte) 0x00,
			(byte) 0x5A, (byte) 0x50, (byte) 0xF7
		});
	}

	private MidiMessage youCanTalk() {
		return customMessageFor(new byte[] {
			(byte) 0xF0, (byte) 0x52, (byte) 0x00,
			(byte) 0x5A, (byte) 0x16, (byte) 0xF7
		});
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
