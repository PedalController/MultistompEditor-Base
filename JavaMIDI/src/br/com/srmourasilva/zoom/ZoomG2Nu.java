package br.com.srmourasilva.zoom;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import br.com.srmourasilva.zoom.effect.Effect;
import br.com.srmourasilva.zoom.effect.ZoomGenericEffect;

public class ZoomG2Nu extends ZoomPedal {

	public static int STATE_ON = 0x7f;
	public static int STATE_OFF= 0x00;

	private static int SIZE_PATCHS = 100;

	/** Number of parameters that can have an Effect
	 *
	 *  Equalizer have 6
	 *  All other have max 3
	 */
	public static int SIZE_PARAMS = 6;

	protected ZoomG2Nu() {}

	/** Very thanks for: 
	 * http://www.loopers-delight.com/LDarchive/201104/msg00195.html
	 */
	protected List<Effect> createEffects() {
		List<Effect> effects = new ArrayList<Effect>();
		effects.add(new ZoomGenericEffect(65, "Comp",       SIZE_PARAMS));
		effects.add(new ZoomGenericEffect(66, "Efx",        SIZE_PARAMS));
		effects.add(new ZoomGenericEffect(68, "Drive",      SIZE_PARAMS));
		effects.add(new ZoomGenericEffect(69, "EQ",         SIZE_PARAMS));
		effects.add(new ZoomGenericEffect(70, "ZNR",        SIZE_PARAMS));
		effects.add(new ZoomGenericEffect(71, "Modulation", SIZE_PARAMS));
		effects.add(new ZoomGenericEffect(72, "Delay",      SIZE_PARAMS));
		effects.add(new ZoomGenericEffect(73, "Reverb",     SIZE_PARAMS));
		effects.add(new ZoomGenericEffect(74, "Mute",       SIZE_PARAMS));
		effects.add(new ZoomGenericEffect(75, "Bypass",     SIZE_PARAMS));

		return effects;
	}

	@Override
	public String getUSBName() {
		return "G2Nu/G2.1Nu";
	}

	@Override
	protected int getSizePaths() {
		return ZoomG2Nu.SIZE_PATCHS;
	}

	@Override
	protected List<MidiMessage> getMessagesSetState(int index, boolean state, Effect effect) {
		List<MidiMessage> messages = new ArrayList<MidiMessage>();
		int stateCode = state ? STATE_ON : STATE_OFF;

		try {
			messages.add(new ShortMessage(SET_STATE_EFFECT, 0, effect.getMidiId(), stateCode));
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}

		return messages;
	}
}