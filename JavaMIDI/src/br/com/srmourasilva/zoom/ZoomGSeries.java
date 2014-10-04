package br.com.srmourasilva.zoom;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.SysexMessage;

import br.com.srmourasilva.zoom.effect.Effect;
import br.com.srmourasilva.zoom.effect.ZoomGenericEffect;
import br.com.srmourasilva.zoom.util.Util;

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

	public static int SIZE_PARAMS = 8;

	/** - Hey man! Listen me!
	 * 
	 * Sequence of bytes that the pedal should be sent in order 
	 * to perform a system message (SysexMessage)
	 * 
	 * To: 04 F0 52 00 | 07 5A 50 F7
	 */
	public static byte[] LISTEN_ME = {
		(byte) 0xF0, 0x52, 0x00,
		0x5A, 0x50, (byte) 0xF7
	};

	/** - Let it be...
	 * 
	 * Sequence of bytes that the pedal should get if you
	 * have nothing to say or want to cancel LISTEN_ME
	 * 
	 * To: 04 F0 52 00 | 07 5A 16 F7
	 */
	public static byte[] LET_IT_BE = {
		(byte) 0xF0, 0x52, 0x00,
		0x5A, 0x16, (byte) 0xF7
	};

	/** INIT - Byte sequence to change the state of a Effect
	 * 
	 * To: 04 F0 52 00 | 04 5A 31
	 */
	public static byte[] INIT_CONF_EFFECT = {
		(byte) 0xF0, 0x52, 0x00,
		0x5A, 0x31
	};

	/** END - Byte sequence to change the state of a Effect
	 * 
	 * 05 F7 00 00
	 */
	public static byte[] END_CONF_EFFECT = {
		(byte) 0xF7
	};

	//04 F0 52 00 | 04 5A 31 00 | 04 00 01 00 | 05 F7 53 00 // Sequência que liga o 1° pedal
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
		List<Effect> effects = new ArrayList<Effect>();
		
		for (int i=0; i < this.sizeEffects; i++) {
			effects.add(new ZoomGenericEffect(i, "Position "+i, SIZE_PARAMS));
		}
	
		return effects;
	}

	/**
	 * INIT_CONF_EFFECT + idPedal + idConf + param1 + param2 + END_CONF_EFFECT
	 */
	private byte[] getMessagePedal(int idEffect, int idParam) {
		return Util.concat(INIT_CONF_EFFECT, END_CONF_EFFECT);
	}

	@Override
	protected List<MidiMessage> getMessagesSetState(int index, boolean state, Effect effect) {
		List<MidiMessage> messages = new ArrayList<MidiMessage>();
		try {
			messages.add(new SysexMessage(LISTEN_ME, LISTEN_ME.length));

			int byteState = state ? 0x01 : 0x00;
			byte[] setState = {
				(byte) 0xF0, 0x52, 0x00,
				0x5A, 0x31, (byte) index,
				0x00, (byte) byteState, 0x00,
				(byte) 0xF7
			};

			messages.add(new SysexMessage(setState, setState.length));

			//http://www.blitter.com/~russtopia/MIDI/~jglatt/tech/midispec/sysex.htm
			//04 F0 52 00 | 04 5A 31 00 | 04 00 01 00 | 05 F7 FF FF // TODO  Como está enviando
			//04 F0 52 00 | 04 5A 31 00 | 04 00 01 00 | 05 F7 53 00 // Sequência que liga o 1° pedal
			//04 F0 52 00 | 04 5A 31 00 | 04 00 00 00 | 05 F7 53 00 // Sequência que desativa 1° pedal
			//04 F0 52 00 | 04 5A 31 01 | 04 00 01 00 | 05 F7 53 00 // Sequência que liga o 2° pedal
			//04 F0 52 00 | 04 5A 31 01 | 04 00 00 00 | 05 F7 53 00 // Sequência que desliga o 2° pedal

			/*
			04 F0 52 00 | 04 5A 17 45 | 04 00 00 00 | 06 00 F7 00 // Pedaleira me disse: Vou tagarelar bem muito uma conversa sem sentido
			04 F0 52 00
			04 5A 28 64
			04 15 1C 40
			04 07 20 00                                                                                              
			04 00 02 00
			04 00 00 00
			04 00 43 28
			04 50 40 11
			04 00 02 34
			04 27 3A 06
			04 18 12 00
			04 4d 18 40
			04 09 00 08
			04 04 16 00
			04 00 00 00
			04 12 00 71
			04 64 43 07
			04 68 01 01
			
			...
			04 44 22 20
			06 00 f7 00 // FIM DA MENSAGEM

//04 F0 52 00 | 07 5A 29 F7 // Talvez isso seja: Pedaleira, vocÊ tem algo a me dizer?
 MSG DO SYSTM     PEDAL 5     VOCÊ FAÇA ALGUMA COISA (Trocar pra nenhum efeito?)
//04 F0 52 00 | 04 5A 31 05 | 04 01 6B 00 | 05 F7 6C 6F
				  Pedal 4
//04 F0 52 00 | 04 5A 31 04 | 04 01 46 00 | 05 F7 6C 6F                                
				  Pedal 4
//04 F0 52 00 | 04 5A 31 04 | 04 01 46 00 | 05 F7 6C 6F PDL MnPit
//04 F0 52 00 | 04 5A 31 04 | 04 01 45 00 | 05 F7 6C 6F PDL Pitch

				 PEDAL 4      HUM... |- Valor
//04 F0 52 00 | 04 5A 31 04 | 04 06 00 00 | 05 F7 6C 6F THE VIBE - mode vibrt                                                       
//04 F0 52 00 | 04 5A 31 04 | 04 06 01 00 | 05 F7 6C 6F THE VIBE - mode chors
								 |- Posição do paramentro
//04 F0 52 00 | 04 5A 31 04 | 04 06 06 00 | 05 F7 6C 6F
//04 F0 52 00 | 04 5A 31 04 | 04 06 13 01 | 05 F7 6C 6F
//04 F0 52 00 | 04 5A 31 04 | 04 06 16 01 | 05 F7 6C 6F

							// 3° byte: 128 é 0x7F. Aí, quando passa, o próximo byte vira 0x01
							// e o 3° byte zera
							// até chegar no máximo, que é 0x16, que é 22, que é 150 - 128
//04 F0 52 00 | 04 5A 31 04 | 04 06 12 00 | 05 F7 6C 6F
//04 F0 52 00 | 04 5A 31 04 | 04 06 13 01 | 05 F7 6C 6F

THE VIBE
//04 F0 52 00 | 04 5A 31 04 | 04 02 00 00 | 05 F7 6C 6F SPEED 1°
//04 F0 52 00 | 04 5A 31 04 | 04 03 3A 00 | 05 F7 6C 6F depth 2°
//04 F0 52 00 | 04 5A 31 04 | 04 04 28 00 | 05 F7 6C 6F BIAS  3°
//04 F0 52 00 | 04 5A 31 04 | 04 05 02 00 | 05 F7 6C 6F WAVE  4°
//04 F0 52 00 | 04 5A 31 04 | 04 06 00 00 | 05 F7 6C 6F MODE 5°
//04 F0 52 00 | 04 5A 31 04 | 04 07 48 00 | 05 F7 6C 6F LEVEL 6°

DZ DRIVER

//EI PEDALEIRA!	CONF EFFECT		 |- Tipo de comando
//04 F0 52 00 | 04 5A 31 04 | 04 00 01 00 | 05 F7 6C 6F LIGAR/DESLIGAR                                                      
                                     |-Parametro
//04 F0 52 00 | 04 5A 31 04 | 04 01 17 00 | 05 F7 6C 6F TROCAR PARA O COMP
//04 F0 52 00 | 04 5A 31 04 | 04 02 1F 00 | 05 F7 6C 6F GAIN 2°
//04 F0 52 00 | 04 5A 31 04 | 04 09 10 00 | 05 F7 6C 6F CAP  8°                                                      
												   |- Não sei o que faz :/
//04 F0 52 00 | 04 5A 31 04 | 04 09 10 00 | 05 F7 6C 6F CAP  8°
 * 													  |- Não sei mesmo :'(
PDL PITCH
//04 F0 52 00 | 04 5A 31 04 | 04 05 01 00 | 05 F7 6C 6F Mode Down

  04 F0 52 00 | 04 5A 31 05 | 04 02 5E 0F | 05 F7 00 00
  04 F0 52 00 | 04 5A 31 05 | 04 02 66 0E | 05 F7 00 00

Trocar pedal 6 com o 5 e vice versa
00000000  04 F0 52 00 04 5A 28 40 04 56 00 00 04 00 00 00 04 00 02 00 04 00 00 00 04 00 56 00 04 00 00 00 04 00 00 00 04 00 00 08 04 00
0000002A  00 00 04 56 00 00 04 00 00 00 04 00 00 00 04 00 00 00 04 20 00 56
00000000  04 00 00 00 04 00 00 00 04 00 00 00 04 00 00 00 04 0A 60 1A 04 0F 0C 20 04 03 31 64 04 24 0A 16 04 00 00 56 04 00 00 00 04 00
0000002A  00 00 04 00 00 00 04 00 00 00 04 00 64 00 04 20 00 20 04 00 03 00
00000000  04 22 20 20 04 20 20 00 04 20 20 20 04 20 20 20 06 00 F7 00                                              
                                          

*/

		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}

		return messages;
	}
}