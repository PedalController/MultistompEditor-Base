package br.com.srmourasilva.pedalboard;

import effect.AmpFender;
import effect.AmpOrange;
import effect.Chorus;
import effect.Comp;
import effect.Distortion;
import effect.Drive;
import effect.Wah;

public class Main {
	public static void main(String[] args) {
		Pedalboard pedalboard = new Pedalboard();
		
		pedalboard.connect(new Input())
				  .connect(new Wah())
				  .connect(new Comp())
				  .connect(new Splitter().split(
					  new Drive().connect(new AmpFender()),
					  new Distortion().connect(new AmpOrange())
				  ).join())
				  .connect(new Chorus())
				  .connect(new Output());
	}

}
