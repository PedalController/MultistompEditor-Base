package br.com.srmourasilva.multistomp.zoom.gseries.decoder.effect;

import javax.sound.midi.MidiMessage;

import br.com.srmourasilva.domain.message.Cause;
import br.com.srmourasilva.domain.message.CommonCause;
import br.com.srmourasilva.domain.message.Messages;
import br.com.srmourasilva.domain.message.Messages.Message;
import br.com.srmourasilva.domain.multistomp.Multistomp;
import br.com.srmourasilva.multistomp.connection.codification.MessageDecoder;

public class ZoomGSeriesPatchEffectsStatusDecoder implements MessageDecoder {

	@Override
	public boolean isForThis(MidiMessage message) {
		return true;
	}

	@Override
	public Messages decode(MidiMessage message, Multistomp multistomp) {
		final int[] PATCHES = new int[] {6+5, 19+5, 33+5, 47+5, 60+5, 74+5};

		Messages messages = Messages.Empty();

		for (int idPedal = 0; idPedal < PATCHES.length; idPedal++) {
			int patch = PATCHES[idPedal];

			boolean actived = this.hasActived(message, patch);
			messages.add(this.generateMessageFor(idPedal, actived));
		}

		return messages;
	}

	private boolean hasActived(MidiMessage message, int position) {
		final byte LSB = 0x01; // Last Significant Bit

		int actived = message.getMessage()[position] & LSB;

		return actived == 1;
	}

    private Message generateMessageFor(int effect, boolean actived) {
        Cause cause = actived ? CommonCause.ACTIVE_EFFECT : CommonCause.DISABLE_EFFECT;

        Message message = new Message(cause);
        message.details().effect = effect;

        return message;
    }
}
