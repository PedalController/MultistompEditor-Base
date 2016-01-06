package br.com.srmourasilva.multistomp.zoom.gseries.decoder;


public class ZoomGSeriesActiveEffectDecoder extends AbstractZoomGSeriesPatchDecoder {

	@Override
	protected int size() {
		return 110;
	}

	@Override
	protected int[] patches() {
		return new int[] {6, 19, 33, 47, 60, 74};
	}
}