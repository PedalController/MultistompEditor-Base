package br.com.srmourasilva.zoom;

import java.util.ArrayList;
import java.util.List;

import br.com.srmourasilva.zoom.effect.Effect;
import br.com.srmourasilva.zoom.effect.ZoomGenericEffect;

/** For:
 *  - Zoom G3
 *  - Zoom G5
 *  - Zoom Ms-50G
 *  - Zoom Ms-70cd
 *  - Zoom MS-200bt
 *  - Zoom MS-50B
 */
public class ZoomGSeries extends ZoomPedal {
	private int sizePatchs;
	private int sizeEffects;

	protected ZoomGSeries(int sizePatchs, int sizeEffects) {
		this.sizePatchs = sizePatchs;
		this.sizeEffects= sizeEffects;
	}
	@Override
	public String getUSBName() {
		return "ZOOM G Series";
	}
	@Override
	protected int getSizePaths() {
		return this.sizePatchs;
	}
	@Override
	protected List<Effect> createEffects() {
		List<Effect> effects = new ArrayList<Effect>();;
		
		for (int i=0; i < this.sizeEffects; i++) {
			effects.add(new ZoomGenericEffect(i, "Position "+i));
		}
	
		return effects; 
	}
}