package br.com.srmourasilva.main;

import java.util.Scanner;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.SysexMessage;

import br.com.srmourasilva.architecture.exception.DeviceNotFoundException;
import br.com.srmourasilva.multistomp.controller.PedalController;
import br.com.srmourasilva.multistomp.controller.PedalControllerFactory;

public class KeyboardController {
	
	private PedalController pedal;
	private Scanner in;

	public static void main(String[] args) {
		new KeyboardController();
	}

	public KeyboardController() {
		this.pedal = init();

		String command = "";
		while (!command.toUpperCase().equals("EXIT")) {
			menu();

			command = getCommand();
			execute(command);
			
			//System.out.println("\n" + pedal);
		}

		this.pedal.off();
	}

	private PedalController init() {
		PedalController pedal = null;
		this.in = new Scanner(System.in);

		try {
			//this.pedal = PedalControllerFactory.getPedal(PedalType.G2Nu);
			pedal = PedalControllerFactory.searchPedal();
			//pedal.addListenner(message -> System.out.println(message));
			pedal.on();

		} catch (DeviceNotFoundException e) {
			System.out.println("Pedal not found! You connected any?");
			System.exit(1);
		} catch (MidiUnavailableException e) {
			System.out.println("This Pedal has been used by other process program");
			System.exit(1);
		}

		return pedal;
	}

	private void menu() {
		System.out.println("Play Command:");
		System.out.println(" - 'GT number': GT = Go to 'number' Patch");
		System.out.println(" - 'AC number': Active 'number' effect");
		System.out.println(" - 'DB number': Disable 'number' effect'");
		System.out.println(" - 'R': Receiver");
		System.out.println(" - 'T': Transmitter");
		System.out.println(" - 'C': Get Number of Current Patch");
		System.out.println(" - 'I': Get Current Patch info");
		System.out.println(" - 'IO': Pedal info of 'number' patch");
		System.out.println(" - '?': Set type effect for [number] effect");

		System.out.println(" - 'Exit': To exit");
	}

	private String getCommand() {
	    System.out.print("Command: ");
	    return in.nextLine();
	}

	private void execute(String command) {
		command = command.toUpperCase();
		String commands[] = command.split(" ");

		String action = commands[0];
		
		if (action.equals("GT"))
			pedal.toPatch(Integer.parseInt(commands[1]));

		else if (action.equals("AC"))
			pedal.activeEffect(Integer.parseInt(commands[1]));

		else if (action.equals("DB"))
			pedal.disableEffect(Integer.parseInt(commands[1]));

		else if (action.equals("R")) {
			byte[] LISSEN_ME = {
				(byte) 0xF0, 0x52, 0x00,
				0x5A, 0x16, (byte) 0xF7
			};

			pedal.sendMessage(customMessage(LISSEN_ME));

		} else if (action.equals("T")) {
			byte[] YOU_CAN_TALK = {
				(byte) 0xF0, 0x52, 0x00,
				0x5A, 0x50, (byte) 0xF7
			};

			pedal.sendMessage(customMessage(YOU_CAN_TALK));
		} else if (action.equals("C")) {
			byte[] CURRENT_PATCH = {
				(byte) 0xF0, 0x52, 0x00,
				0x5A, 0x33, (byte) 0xF7
			};
			
			pedal.sendMessage(customMessage(CURRENT_PATCH));
		} else if (action.equals("I")) {
			byte[] CURRENT_PATCH = {
				(byte) 0xF0, 0x52, 0x00,
				0x5A, 0x29, (byte) 0xF7
			};
			
			pedal.sendMessage(customMessage(CURRENT_PATCH));

		} else if (action.equals("IO")) {
			byte[] CURRENT_PATCH = {
				(byte) 0xF0, 0x52, 0x00,
				0x5A, 0x09, (byte) 0x00,
				0x00, (byte) Integer.parseInt(commands[1]), (byte) 0xF7
			};
			
			pedal.sendMessage(customMessage(CURRENT_PATCH));
		} else if (action.equals("?")) {
			byte[] SET_EFFECT_TYPE = {
				(byte) 0xF0, (byte) 0x52, (byte) 0x00,
				(byte) 0x5A, (byte) 0x31, (byte) Integer.parseInt(commands[1]),
				(byte) 0x01, (byte) 0x15, (byte) 0x00,
				(byte) 0xf7         // |- 0 a 117
			};
			
			pedal.sendMessage(customMessage(SET_EFFECT_TYPE));
		}
	}

	private SysexMessage customMessage(byte[] message) {
		try {
			return new SysexMessage(message, message.length);
		} catch (InvalidMidiDataException e) {
			throw new RuntimeException(e);
		}
	}
}