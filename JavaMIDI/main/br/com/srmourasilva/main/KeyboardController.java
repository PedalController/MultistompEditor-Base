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
		init();

		String command = "";
		while (!command.toUpperCase().equals("EXIT")) {
			menu();

			command = getCommand();
			execute(command);
		}

		this.pedal.off();
	}

	private void init() {
		this.in = new Scanner(System.in);

		try {
			//this.pedal = PedalControllerFactory.getPedal(PedalType.G2Nu);
			this.pedal = PedalControllerFactory.searchPedal();
		} catch (DeviceNotFoundException e) {
			System.out.println("Pedal not found! You connected any?");
			e.printStackTrace();
			System.exit(1);
		}

		try {
			this.pedal.on();
		} catch (MidiUnavailableException e) {
			System.out.println("This Pedal has been used by other process program");
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void menu() {
		System.out.println("Play Command:");
		System.out.println(" - 'GT number': GT = Go to 'number' Patch");
		System.out.println(" - 'AC number': Active 'number' effect");
		System.out.println(" - 'DB number': Disable 'number' effect'");
		System.out.println(" - 'R': Receiver");
		System.out.println(" - 'T': Transmitter");
		System.out.println(" - 'Exit': To exit");
	}

	private String getCommand() {
	    System.out.print("Command: ");
	    return in.nextLine();
	}

	private void execute(String command) {
		command = command.toUpperCase();
		String commands[] = command.split(" ");

		if (commands[0].equals("GT"))
			pedal.setPatch(Integer.parseInt(commands[1]));

		else if (commands[0].equals("AC"))
			pedal.activeEffect(Integer.parseInt(commands[1]));

		else if (commands[0].equals("DB"))
			pedal.disableEffect(Integer.parseInt(commands[1]));

		else if (commands[0].equals("R")) {
			byte[] LISSEN_ME = {
				(byte) 0xF0, 0x52, 0x00,
				0x5A, 0x16, (byte) 0xF7
			};

			try {
				pedal.sendMessage(new SysexMessage(LISSEN_ME, LISSEN_ME.length));
			} catch (InvalidMidiDataException e) {
				e.printStackTrace();
			}

		} else if (commands[0].equals("T")) {
			byte[] YOU_CAN_TALK = {
					(byte) 0xF0, 0x52, 0x00,
					0x5A, 0x50, (byte) 0xF7
				};

				try {
					pedal.sendMessage(new SysexMessage(YOU_CAN_TALK, YOU_CAN_TALK.length));
				} catch (InvalidMidiDataException e) {
					e.printStackTrace();
				}
			}
	}
}
