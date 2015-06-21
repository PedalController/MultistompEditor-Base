package br.com.srmourasilva.editshare;

import javax.sound.midi.MidiUnavailableException;

import br.com.srmourasilva.architecture.exception.DeviceNotFoundException;
import br.com.srmourasilva.domain.OnMultistompListenner;
import br.com.srmourasilva.domain.message.CommonCause;
import br.com.srmourasilva.domain.message.Messages;
import br.com.srmourasilva.domain.message.Messages.Message;
import br.com.srmourasilva.multistomp.controller.PedalController;
import br.com.srmourasilva.multistomp.controller.PedalControllerFactory;
import br.com.srmourasilva.multistomp.zoom.gseries.ZoomGSeriesMessages;

public class EasyEditSharePresenter implements OnMultistompListenner {

	private EasyEditShare view;

	private PedalController pedal;	

	public EasyEditSharePresenter(EasyEditShare view) {
		this.view = view;
	}

	public void start() {
		try {
			this.pedal = PedalControllerFactory.searchPedal();
			pedal.addListenner(this);
			pedal.on();

		} catch (DeviceNotFoundException e) {
			System.out.println("Pedal not found! You connected any?");
			System.exit(1);
		} catch (MidiUnavailableException e) {
			System.out.println("This Pedal has been used by other process program");
			System.exit(1);
		}

		pedal.send(ZoomGSeriesMessages.REQUEST_CURRENT_PATCH_NUMBER());
	}

	@Override
	public void onChange(Messages messages) {
		messages.get(CommonCause.ACTIVE_EFFECT).forEach(message -> updateEffect(message, CommonCause.ACTIVE_EFFECT));
		messages.get(CommonCause.DISABLE_EFFECT).forEach(message -> updateEffect(message, CommonCause.DISABLE_EFFECT));

		messages.get(CommonCause.TO_PATCH).forEach(message -> setPatch(message));
		
		messages.get(CommonCause.SET_PARAM).forEach(message -> System.out.println(pedal));
	}

	private void updateEffect(Message message, CommonCause cause) {
		int patch  = message.details().patch;
		int effect = message.details().effect;

		if (patch != pedal.multistomp().getIdCurrentPatch())
			return;

		if (cause == CommonCause.ACTIVE_EFFECT)
			view.active(effect);
		else
			view.disable(effect);
	}
	
	private void setPatch(Message message) {
		int idPatch = message.details().patch;
		view.setTitle("Patch: " + idPatch);

		pedal.send(ZoomGSeriesMessages.REQUEST_SPECIFIC_PATCH_DETAILS(idPatch));
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