package br.com.srmourasilva.multieffects.zoom;

import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import br.com.srmourasilva.architecture.Observable;
import br.com.srmourasilva.multieffects.Effect;
import br.com.srmourasilva.multieffects.Param;
import br.com.srmourasilva.multieffects.Patch;
import br.com.srmourasilva.multieffects.Pedal;

/**
 * As pedaleiras Zoom possuem muitos patchs, muitos
 * efeitos e parâmetros. Entretanto, não consigo
 * descobrir (pegar da pedaleira) os efeitos
 * 
 * Como aqui oferece uma opção cega para alteração 
 * dos efeitos, o ZoomPedal possui uma Lista de Patchs.
 * No entanto, todos os patchs apontam para os mesmos efeitos!
 * Mas cada efeito possui seus parâmetros 
 * 
 * FIXME - Quando eu descobrir como pegar a lista de efeitos direto da
 * pedaleira, aí vou ter que ajeitar isto aqui!
 */
public abstract class ZoomPedal extends Pedal {

	public static int SET_PATH = ShortMessage.PROGRAM_CHANGE;
	public static int SET_STATE_EFFECT= ShortMessage.CONTROL_CHANGE;

	private List<Effect> effects;

	public ZoomPedal() {}

	/*************************************************/

	@Override
	public void initialize() {}

	@Override
	public void terminate() {}


	/*************************************************/

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


	/*************************************************/

	@Deprecated
	public final void activeEffect(int index) {
		setStatePedal(index, true);
	}

	@Deprecated
	public final void disableEffect(int index) {
		setStatePedal(index, false);
	}

	// FIXME
	private final void setStatePedal(int index, boolean state) {
		if (index >= effects.size() || index < 0) {
			return;
		}

		this.setStatePedal(index, state, effects.get(index));
	}

	/**
	 * @param index  Index in pedal (pedaleira)
	 * @param state  New state
	 * @param effect Effect that will be updated 
	 */
	@Deprecated
	private final void setStatePedal(int index, boolean state, Effect effect) {
		effect.setState(state);

		for (MidiMessage message : getMessagesSetState(index, state, effect)) {
			this.updateAll(message);
		}
	}

	/** Return the MIDI message that will be change the Effect Status
	 * 
	 * @param index  Index of Effect in Pedal
	 * @param state  New state
	 * @param effect Effect that will be changed state 
	 */
	protected abstract List<MidiMessage> getMessagesSetState(int index, boolean state, Effect effect);


	/*************************************************/

	/** O Patch sofreu mudança ou um Efeito ou então um Parâmetro
	 */
	@Override
	public final void update(Observable observable) {
		System.out.print("Patch: ");
		System.out.println(observable instanceof Patch);
		System.out.print("Effect: ");
		System.out.println(observable instanceof Effect);
		System.out.print("Param: ");
		System.out.println(observable instanceof Param);
	}
}