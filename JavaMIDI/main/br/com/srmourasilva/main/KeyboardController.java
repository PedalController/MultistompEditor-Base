package br.com.srmourasilva.main;

import java.util.Scanner;

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
		//this.pedal = PedalControllerFactory.getPedal(PedalType.G2Nu);
		this.pedal = PedalControllerFactory.searchPedal();
		
		this.pedal.on();
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

		if (commands[0].equals("GT"))
			pedal.setPatch(Integer.parseInt(commands[1]));

		else if (commands[0].equals("AC"))
			pedal.activeEffect(Integer.parseInt(commands[1]));

		else if (commands[0].equals("DB"))
			pedal.disableEffect(Integer.parseInt(commands[1]));
	}
}
