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

	protected ZoomG2Nu() {}

	/** Very thanks for: 
	 * http://www.loopers-delight.com/LDarchive/201104/msg00195.html
	 */
	protected List<Effect> createEffects() {
		List<Effect> effects = new ArrayList<Effect>();
		effects.add(new ZoomGenericEffect(65, "Comp"));
		effects.add(new ZoomGenericEffect(66, "Efx"));
		effects.add(new ZoomGenericEffect(68, "Drive"));
		effects.add(new ZoomGenericEffect(69, "EQ"));
		effects.add(new ZoomGenericEffect(70, "ZNR"));
		effects.add(new ZoomGenericEffect(71, "Modulation"));
		effects.add(new ZoomGenericEffect(72, "Delay"));
		effects.add(new ZoomGenericEffect(73, "Reverb"));
		effects.add(new ZoomGenericEffect(74, "Mute"));
		effects.add(new ZoomGenericEffect(75, "Bypass"));

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