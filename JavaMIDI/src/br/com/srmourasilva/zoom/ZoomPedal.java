package br.com.srmourasilva.zoom;

import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

import br.com.srmourasilva.zoom.effect.Effect;
import br.com.srmourasilva.zoom.effect.Patch;

public abstract class ZoomPedal extends Pedal {
	
	public static int STATE_ON = 0x7f;
	public static int STATE_OFF= 0x00;

	public static int SET_PATH = ShortMessage.PROGRAM_CHANGE;
	public static int SET_STATE_EFFECT= ShortMessage.CONTROL_CHANGE;

	private List<Effect> effects;

	@Override
	public void init() {
		for (int i = 0; i < this.getSizePaths(); i++) {
			this.addPatch(new Patch(i));
		}
		effects = createEffects();
	}

	protected abstract int getSizePaths();

	public final void setPatchImpl(int index) {
		try {
			ShortMessage message = new ShortMessage(SET_PATH, 0, index, 0);
			this.updateAll(message);

		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public final void activeEffect(int index) {
		setStatePedal(index, true);
	}
	@Override
	public final void disableEffect(int index) {
		setStatePedal(index, false);
	}
	private final void setStatePedal(int index, boolean state) {
		System.out.println(effects.size());
		if (index >= effects.size() || index < 0) {
			return;
		}
		this.setStatePedal(index, state, effects.get(index));
	}

	// A montagem da mensagem muda dependendo do TypePedal
	@Override
	public final void setStatePedal(int index, boolean state, Effect effect) {
		try {
			// Para a G2
			effect.setState(state);

			int stateCode = state ? STATE_ON : STATE_OFF;
			//MidiMessage message = new ShortMessage(0x80, 0, 0x31, 00);

			// Para a G3
			byte[] bytes = {(byte) 0xF0, 0x52, 0x00, 0x5A, 0x50, (byte) 0xF7}; // Sequência que diz que vai fazer alguma coisa
			// 04 F0 52 00 | 07 5A 50 F7
			//byte[] bytes = 04 F0 52 00 | 07 5A 16 F7 //Sequência que diz que vai fazer nada

			MidiMessage message = new SysexMessage(bytes, bytes.length);
			//this.updateAll(message);

			byte[] bytes2 = {(byte) 0xF0, 0x52, 0x00, 0x5A, 0x31, 0x00, 0x00, 0x00, 0x00, (byte) 0xF7, 0x53, 0x00}; // Sequência que desativa primeiro pedal
			message = new SysexMessage(bytes2, bytes2.length);
			this.updateAll(message);

			//http://www.blitter.com/~russtopia/MIDI/~jglatt/tech/midispec/sysex.htm
			//04 F0 52 00 | 04 5A 31 00 | 04 00 01 00 | 05 F7 53 00 // Sequência que liga o 1° pedal
			//04 F0 52 00 | 04 5A 31 00 | 04 00 00 00 | 05 F7 53 00 // Sequência que desativa 1° pedal
			//04 F0 52 00 | 04 5A 31 01 | 04 00 01 00 | 05 F7 53 00 // Sequência que liga o 2° pedal
			//04 F0 52 00 | 04 5A 31 01 | 04 00 00 00 | 05 F7 53 00 // Sequência que desliga o 2° pedal

			this.updateAll(message);

		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
}