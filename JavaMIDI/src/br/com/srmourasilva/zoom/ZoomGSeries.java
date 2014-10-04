package br.com.srmourasilva.zoom;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.SysexMessage;

import br.com.srmourasilva.zoom.effect.Effect;
import br.com.srmourasilva.zoom.effect.ZoomGenericEffect;

/** For:
 *  - Zoom G3
 *  - Zoom G5
 *  - Zoom Ms-50G
 *  - Zoom Ms-70cd
 *  - Zoom MS-200bt
 *  - Zoom MS-50B
 */
public class ZoomGSeries extends ZoomPedal {
	private int sizePatchs;
	private int sizeEffects;

	/** - Hey man! Listen me!
	 * 
	 * Sequence of bytes that the pedal should be sent in order 
	 * to perform a system message (SysexMessage)
	 * 
	 * To: 04 F0 52 00 | 07 5A 50 F7
	 */
	public static byte[] LISTEN_ME = {
		(byte) 0xF0, 0x52, 0x00,
		0x5A, 0x50, (byte) 0xF7
	};

	/** - Let it be...
	 * 
	 * Sequence of bytes that the pedal should get if you
	 * have nothing to say or want to cancel LISTEN_ME
	 * 
	 * To: 04 F0 52 00 | 07 5A 16 F7
	 */
	public static byte[] LET_IT_BE = {
		(byte) 0xF0, 0x52, 0x00,
		0x5A, 0x16, (byte) 0xF7
	};

	protected ZoomGSeries(int sizePatchs, int sizeEffects) {
		this.sizePatchs = sizePatchs;
		this.sizeEffects= sizeEffects;
	}

	@Override
	public String getUSBName() {
		return "ZOOM G Series";
	}

	@Override
	protected int getSizePaths() {
		return this.sizePatchs;
	}

	@Override
	protected List<Effect> createEffects() {
		List<Effect> effects = new ArrayList<Effect>();
		
		for (int i=0; i < this.sizeEffects; i++) {
			effects.add(new ZoomGenericEffect(i, "Position "+i));
		}
	
		return effects;
	}

	@Override
	protected List<MidiMessage> getMessagesSetState(int index, boolean state, Effect effect) {
		List<MidiMessage> messages = new ArrayList<MidiMessage>();
		try {
			messages.add(new SysexMessage(LISTEN_ME, LISTEN_ME.length));

			int byteState = state ? 0x01 : 0x00;
			byte[] setState = {
				(byte) 0xF0, 0x52, 0x00,
				0x5A, 0x31, (byte) index,
				0x00, (byte) byteState, 0x00,
				(byte) 0xF7
			};

			messages.add(new SysexMessage(setState, setState.length));

			//http://www.blitter.com/~russtopia/MIDI/~jglatt/tech/midispec/sysex.htm
			//04 F0 52 00 | 04 5A 31 00 | 04 00 01 00 | 05 F7 FF FF // TODO  Como está enviando
			//04 F0 52 00 | 04 5A 31 00 | 04 00 01 00 | 05 F7 53 00 // Sequência que liga o 1° pedal
			//04 F0 52 00 | 04 5A 31 00 | 04 00 00 00 | 05 F7 53 00 // Sequência que desativa 1° pedal
			//04 F0 52 00 | 04 5A 31 01 | 04 00 01 00 | 05 F7 53 00 // Sequência que liga o 2° pedal
			//04 F0 52 00 | 04 5A 31 01 | 04 00 00 00 | 05 F7 53 00 // Sequência que desliga o 2° pedal

		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}

		return messages;
	}
}