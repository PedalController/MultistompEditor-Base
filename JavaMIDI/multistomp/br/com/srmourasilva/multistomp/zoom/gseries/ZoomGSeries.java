package br.com.srmourasilva.multistomp.zoom.gseries;

import java.util.ArrayList;
import java.util.List;

import br.com.srmourasilva.domain.multistomp.Effect;
import br.com.srmourasilva.domain.multistomp.Multistomp;
import br.com.srmourasilva.domain.multistomp.Patch;
import br.com.srmourasilva.multieffects.PedalType;
import br.com.srmourasilva.multistomp.zoom.ZoomGenericEffect;

/** For:
 *  - Zoom G3
 *  - Zoom G5
 *  - Zoom Ms-50G
 *  - Zoom Ms-70cd
 *  - Zoom MS-200bt
 *  - Zoom MS-50B
 */
public class ZoomGSeries extends Multistomp {

	private final int TOTAL_PATCHS;
	private final int TOTAL_EFFECTS;
	private final int SIZE_PARAMS;

	/**
	 * @param sizePatchs   Max Patches that Pedal may have  
	 * @param totalEffects Max Effects that Patches may have
	 */
	public ZoomGSeries(int totalPatchs, int totalEffects, int totalParams) {
		TOTAL_PATCHS = totalPatchs;
		TOTAL_EFFECTS = totalEffects;
		SIZE_PARAMS = totalParams;

		List<Patch> patchs = createPatchs(TOTAL_PATCHS);

		for (Patch patch : patchs)
			this.addPatch(patch);
	}

	private List<Patch> createPatchs(int totalPatch) {
		List<Patch> patchs = new ArrayList<>();

		for (int i=0; i<totalPatch; i++) {
			Patch patch = new Patch(i);
			for (Effect effect : this.createEffects(TOTAL_EFFECTS))
				patch.addEffect(effect);

			patchs.add(patch);
		}

		return patchs;
	}

	private List<Effect> createEffects(int totalEffects) {
		List<Effect> effects = new ArrayList<Effect>();

		for (int i=0; i < totalEffects; i++)
			effects.add(new ZoomGenericEffect(i, "Position "+i, SIZE_PARAMS));

		return effects;
	}

	@Override
	public void initialize() {}

	@Override
	public void terminate() {}

	@Override
	public PedalType getPedalType() {
		return PedalType.G3; // FIXME
	}
}