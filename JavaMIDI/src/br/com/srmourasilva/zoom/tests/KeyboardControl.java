package br.com.srmourasilva.zoom.tests;

import java.util.Scanner;

import br.com.srmourasilva.zoom.Pedal;
import br.com.srmourasilva.zoom.ZoomFactory;
import br.com.srmourasilva.zoom.ZoomFactory.PedalType;
import br.com.srmourasilva.zoom.midi.MidiSender;

public class KeyboardControl {
	private Pedal pedal;	
	private Scanner in;
	private MidiSender sender;

	public static void main(String[] args) {
		new KeyboardControl();
	}

	
	public KeyboardControl() {
		init();
		
		String command = "";
		while (!command.toUpperCase().equals("EXIT")) {
			menu();
			command = getCommand();
			execute(command);
		}

		bye();
	}

	private void init() {
		this.in = new Scanner(System.in);
		this.pedal = ZoomFactory.getPedal(PedalType.G2Nu);

		sender = new MidiSender(this.pedal);
		sender.start();
	}

	private void menu() {
		System.out.println("Play Command:");
		System.out.println(" - 'GT number': GT = Go to 'number' Patch");
		System.out.println(" - 'AC number': Active 'number' effect");
		System.out.println(" - 'DB number': Disable 'number' effect'");
		System.out.println(" - 'Exit': To exit");
	}

	private String getCommand() {
	    System.out.print("Command: ");
	    return in.nextLine();
	}
	
	private void execute(String command) {
		command = command.toUpperCase();
		String commands[] = command.split(" ");

		if (commands[0].equals("GT")) {
			pedal.setPatch(Integer.parseInt(commands[1]));

		} else if (commands[0].equals("AC")) {
			pedal.activeEffect(Integer.parseInt(commands[1]));

		} else if (commands[0].equals("DB")) {
			pedal.disableEffect(Integer.parseInt(commands[1]));
		}
	}

	private void bye() {
		sender.stop();
	}
}
