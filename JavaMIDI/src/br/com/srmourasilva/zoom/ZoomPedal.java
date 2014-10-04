package br.com.srmourasilva.zoom;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import br.com.srmourasilva.zoom.effect.Patch;

public abstract class ZoomPedal extends Pedal {

	public static int SET_PATH = ShortMessage.PROGRAM_CHANGE;
	public static int SET_STATE_EFFECT= ShortMessage.CONTROL_CHANGE;

	@Override
	public void init() {
		for (int i = 0; i < this.getSizePaths(); i++) {
			this.addPatch(new Patch(i));
		}
		this.setEffects(createEffects());
	}

	/** Number of paths that the Pedal has
	 */
	protected abstract int getSizePaths();

	@Override
	protected MidiMessage getMessageSetPatch(int idPatch) {
		MidiMessage message = null;
		try {
			message = new ShortMessage(SET_PATH, 0, idPatch, 0);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		return message;
	}
}