package br.com.srmourasilva.editshare;

import javax.sound.midi.MidiUnavailableException;

import br.com.srmourasilva.architecture.exception.DeviceNotFoundException;
import br.com.srmourasilva.domain.OnMultistompListener;
import br.com.srmourasilva.domain.message.CommonCause;
import br.com.srmourasilva.domain.message.Messages;
import br.com.srmourasilva.domain.message.Messages.Message;
import br.com.srmourasilva.multistomp.controller.PedalController;
import br.com.srmourasilva.multistomp.controller.PedalControllerFactory;
import br.com.srmourasilva.multistomp.zoom.gseries.ZoomGSeriesMessages;

public class EasyEditSharePresenter implements OnMultistompListener {

	private EasyEditShare view;

	private PedalController pedal;

	public EasyEditSharePresenter(EasyEditShare view) {
		this.view = view;
	}

	public void start() {
		try {
			this.pedal = PedalControllerFactory.searchPedal();
		} catch (DeviceNotFoundException e) {
			view.setTitle("Pedal not found! You connected any?");
			return;
		}

		pedal.addListener(this);

		try {
			pedal.on();
		} catch (MidiUnavailableException e) {
			view.setTitle("This Pedal has been used by other process program");
			return;
		}

		pedal.send(ZoomGSeriesMessages.REQUEST_CURRENT_PATCH_NUMBER());
	}

	public void stop() {
		if (pedal != null)
			pedal.off();
	}

	@Override
	public void onChange(Messages messages) {
		messages.getBy(CommonCause.EFFECT_ACTIVE).forEach(message -> updateEffect(message, CommonCause.EFFECT_ACTIVE));
		messages.getBy(CommonCause.EFFECT_DISABLE).forEach(message -> updateEffect(message, CommonCause.EFFECT_DISABLE));

		messages.getBy(CommonCause.TO_PATCH).forEach(message -> setPatch(message));
		messages.getBy(CommonCause.PATCH_NAME).forEach(message -> updateTitle((String) message.details().value));
		
		//messages.getBy(CommonCause.PARAM_CHANGED).forEach(message -> System.out.println(pedal));

		//messages.getBy(CommonCause.EFFECT_CHANGED).forEach(message -> System.out.println(message));
	}

	private void updateEffect(Message message, CommonCause cause) {
		int patch  = message.details().patch;
		int effect = message.details().effect;

		boolean otherPatch = patch != pedal.multistomp().getIdCurrentPatch();
		if (otherPatch)
			return;

		if (cause == CommonCause.EFFECT_ACTIVE)
			view.active(effect);
		else
			view.disable(effect);
	}
	
	private void setPatch(Message message) {
		int idPatch = message.details().patch;

		pedal.send(ZoomGSeriesMessages.REQUEST_SPECIFIC_PATCH_DETAILS(idPatch));
	}
	

	private void updateTitle(String other) {
		view.setTitle(pedal.multistomp().currentPatch().toString());
	}


	public void toogleEffectOf(int effect) {
		this.pedal.toogleEffect(effect);
	}
	
	/////////////////////////////////////////////////////

	public void nextPatch() {
		this.pedal.nextPatch();
	}
	
	public void beforePatch() {
		this.pedal.beforePatch();
	}
}