package br.com.srmourasilva.multieffects.nulo.pedal;

import javax.sound.midi.MidiMessage;

import br.com.srmourasilva.architecture.Observable;
import br.com.srmourasilva.multieffects.Patch;
import br.com.srmourasilva.multieffects.Pedal;
import br.com.srmourasilva.multieffects.PedalType;

public class NullPedal extends Pedal {

	private String MSG_ERRO = "Pedal Unknown is unimplemented";

	public NullPedal() {
		this.addPatch(new Patch(0));
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initialize() {
		System.out.println(MSG_ERRO);
	}

	@Override
	public void terminate() {
		System.out.println(MSG_ERRO);
	}

	@Override
	public PedalType getPedalType() {
		return PedalType.Null;
	}

	// FIXME - DONT RETURN NULL!
	@Override
	protected MidiMessage getMessageSetPatch(int idPatch) {
		System.out.println(MSG_ERRO);
		return null;
	}

	@Override
	public void update(Observable observable) {}
}