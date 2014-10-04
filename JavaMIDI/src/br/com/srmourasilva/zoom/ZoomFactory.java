package br.com.srmourasilva.zoom;

public class ZoomFactory {
	public enum PedalType {
		Null(0), G2Nu(1), G3(2);

	    private int id;

        private PedalType(int id) {
            this.id = id;
        }
        public int getId() {
        	return id;
        }
	}

	public static Pedal getPedal(PedalType type) {
		Pedal pedal;

		if (type == PedalType.G2Nu) {
			pedal = new ZoomG2Nu();
		} else if (type == PedalType.G3) {
			pedal = new ZoomGSeries(100, 6);
		} else {
			pedal = new ZoomGSeries(0, 0);
		}

		pedal.init();

		return pedal;
	}
}
