package br.com.srmourasilva.multistomp.zoom.gseries;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

import br.com.srmourasilva.domain.message.ChangeMessage;
import br.com.srmourasilva.domain.multistomp.Effect;
import br.com.srmourasilva.domain.multistomp.Multistomp;
import br.com.srmourasilva.multistomp.zoom.ZoomMessageEncoder;

public class ZoomGSeriesMessageEncoder extends ZoomMessageEncoder {

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
		final int SET_PATH = ShortMessage.PROGRAM_CHANGE;
		
		List<MidiMessage> mensagens = new ArrayList<>();
		mensagens.add(new ShortMessage(SET_PATH, 0, message.causer().getIdCurrentPatch(), 0));
		
		return mensagens;
	}

	private List<MidiMessage> encodeEffectChange(ChangeMessage<Multistomp> message) throws InvalidMidiDataException {
		ChangeMessage<Effect> effectMessage = (ChangeMessage<Effect>) message.nextMessage().nextMessage();

		int index = effectMessage.causer().getMidiId();
		boolean actived = effectMessage.causer().hasActived();
		
		int byteActived = actived ? 0x01 : 0x00;

		List<MidiMessage> mensagens = new ArrayList<>();
		mensagens.add(myTurn());
		mensagens.add(effect(index, byteActived));
		
		return mensagens;
	}
	
	private MidiMessage myTurn() throws InvalidMidiDataException {
		final byte[] MY_TURN = {
			(byte) 0xF0, 0x52, 0x00,
			0x5A, 0x50, (byte) 0xF7
		};

		return new SysexMessage(MY_TURN, MY_TURN.length);
	}
	
	private MidiMessage effect(int idParam, int newValue) throws InvalidMidiDataException {
		byte[] setState = {
			(byte) 0xF0, 0x52, 0x00,
			0x5A, 0x31, (byte) idParam,
			0x00, (byte) newValue, 0x00,
			(byte) 0xF7
		};
		
		return new SysexMessage(setState, setState.length);
	}
}
