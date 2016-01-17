package br.com.srmourasilva.pipedalcontroller.domain;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.sound.midi.MidiUnavailableException;

import br.com.srmourasilva.domain.OnMultistompListener;
import br.com.srmourasilva.domain.message.CommonCause;
import br.com.srmourasilva.domain.message.Messages;
import br.com.srmourasilva.domain.message.Messages.Message;
import br.com.srmourasilva.multistomp.controller.PedalController;
import br.com.srmourasilva.multistomp.zoom.gseries.ZoomGSeriesMessages;
import br.com.srmourasilva.pipedalcontroller.domain.clicable.Clicable;

public class PhysicalPedalController implements OnMultistompListener {

	private PedalController pedal;
	private Pedalboard pedalboard;

	public PhysicalPedalController(PedalController pedal) {
		this.pedal = pedal;
		this.pedal.addListener(this);		
		this.pedalboard = new Pedalboard();
	}

	public void vinculePedalEffects(PhysicalEffect ... effects) {
		for (PhysicalEffect physicalEffect : effects) {
			pedalboard.add(physicalEffect);
			physicalEffect.setOnFootswitchClickListener(effectPosition -> {
				System.out.println("Effect Position clicked: " + effectPosition);
				this.pedal.toogleEffect(effectPosition);
			});
		}
	}

	public void vinculeNext(Clicable next) {
		next.setListener(clicable -> pedal.nextPatch());
	}

	public void vinculeBefore(Clicable next) {
		next.setListener(clicable -> pedal.beforePatch());
	}

	public void start() throws MidiUnavailableException {
		pedal.on();
		pedal.send(ZoomGSeriesMessages.REQUEST_CURRENT_PATCH_NUMBER());
	}

	//////////////////////////////////////////

	@Override
	public void onChange(Messages messages) {
		System.out.println(messages);

		messages.getBy(CommonCause.EFFECT_ACTIVE).forEach(message -> updateEffect(message, CommonCause.EFFECT_ACTIVE));
		messages.getBy(CommonCause.EFFECT_DISABLE).forEach(message -> updateEffect(message, CommonCause.EFFECT_DISABLE));

		messages.getBy(CommonCause.TO_PATCH).forEach(message -> setPatch(message));
		
		messages.getBy(CommonCause.EFFECT_TYPE).forEach(message -> updateEffect(message, CommonCause.EFFECT_TYPE));
	}

	private void updateEffect(Message message, CommonCause cause) {
		int patch  = message.details().patch;
		int effect = message.details().effect;

		boolean otherPatch = patch != pedal.multistomp().getIdCurrentPatch();
		if (otherPatch)
			return;

		if (cause == CommonCause.EFFECT_ACTIVE)
			pedalboard.active(effect);

		else if (cause == CommonCause.EFFECT_DISABLE)
			pedalboard.disable(effect);

		else if (cause == CommonCause.EFFECT_TYPE)
			pedalboard.updateEffectType(effect, pedal.multistomp().currentPatch().effects().get(effect).getName());
	}

	
	private void setPatch(Message message) {
		int idPatch = message.details().patch;

		pedal.send(ZoomGSeriesMessages.REQUEST_SPECIFIC_PATCH_DETAILS(idPatch));
	}

	//////////////////////////////////////////

	private static class Pedalboard {
		private Map<Integer, PhysicalEffect> effects = new HashMap<>();

		public void add(PhysicalEffect effect) {
			effects.put(effect.getPosition(), effect);
		}

		public void active(int position) {
			Optional<PhysicalEffect> physicalEffect = getEffect(position);

			if (physicalEffect.isPresent())
				physicalEffect.get().activeLed();
		}

		public void disable(int position) {
			Optional<PhysicalEffect> physicalEffect = getEffect(position);

			if (physicalEffect.isPresent())
				physicalEffect.get().disableLed();
		}
		
		private Optional<PhysicalEffect> getEffect(int position) {
			PhysicalEffect physicalEffect = effects.get(position);

			return physicalEffect == null ? Optional.empty() : Optional.of(physicalEffect);
		}
		
		public void updateEffectType(int position, String pedalName) {
			Optional<PhysicalEffect> effect = getEffect(position);
			if (!effect.isPresent())
				return;

			String imageName = System.getProperty("user.dir") + File.separator + "lib" + File.separator;
			imageName += pedalName + ".png";

			try {
				Image image = ImageIO.read(new File(imageName));
				effect.get().updateDisplay(image);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}
}
