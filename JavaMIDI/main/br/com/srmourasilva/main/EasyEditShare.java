package br.com.srmourasilva.main;

import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.SysexMessage;

import br.com.srmourasilva.architecture.exception.DeviceNotFoundException;
import br.com.srmourasilva.domain.OnChangeListenner;
import br.com.srmourasilva.domain.message.ChangeMessage;
import br.com.srmourasilva.domain.message.CommonCause;
import br.com.srmourasilva.domain.multistomp.Effect;
import br.com.srmourasilva.domain.multistomp.Multistomp;
import br.com.srmourasilva.domain.multistomp.Patch;
import br.com.srmourasilva.multistomp.controller.PedalController;
import br.com.srmourasilva.multistomp.controller.PedalControllerFactory;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class EasyEditShare extends Application implements EventHandler<ActionEvent>, OnChangeListenner<Multistomp> {

	private PedalController pedal;
	private Button[] buttons;

	@Override
	public void start(Stage primaryStage) {
		try {
			this.pedal = PedalControllerFactory.searchPedal();
			pedal.on();
			pedal.addListenner(this);

		} catch (DeviceNotFoundException e) {
			System.out.println("Pedal not found! You connected any?");
			System.exit(1);
		} catch (MidiUnavailableException e) {
			System.out.println("This Pedal has been used by other process program");
			System.exit(1);
		}

		GridPane grid = new GridPane();

		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		this.buttons = createButtons(grid);

		createConfButtons(grid);
		
		Scene scene = new Scene(grid, 400, 250);

		primaryStage.setTitle("Pedals Status");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void createConfButtons(GridPane grid) {
		byte[] NUMBER_CURRENT_PATCH = {
			(byte) 0xF0, 0x52, 0x00,
			0x5A, 0x33, (byte) 0xF7
		};

		byte[] CURRENT_PATCH = {
			(byte) 0xF0, 0x52, 0x00,
			0x5A, 0x29, (byte) 0xF7
		};

		Button currentPatch = new Button();
		currentPatch.setText("Number Current Patch");
		currentPatch.setOnAction(event -> pedal.sendMessage(customMessage(NUMBER_CURRENT_PATCH)));

		Button loadState = new Button();
		loadState.setText("Current Patch - Load");
		loadState.setOnAction(event -> pedal.sendMessage(customMessage(CURRENT_PATCH)));

		grid.add(currentPatch, 4, 2);
		grid.add(loadState, 4, 3);
	}

	private SysexMessage customMessage(byte[] message) {
		try {
			return new SysexMessage(message, message.length);
		} catch (InvalidMidiDataException e) {
			throw new RuntimeException(e);
		}
	}

	private Button[] createButtons(GridPane grid) {
		Button[] buttons = new Button[6];
		for (int i = 0; i < 6; i++) {
			Button button = createButton(i);
			buttons[i] = button;
			grid.add(button, i%3, i/3 < 1 ? 0 : 1);
		}
		
		return buttons;
	}

	private Button createButton(int id) {
		Button btn = new Button();
		btn.setText("Pedal " + id);
		btn.setOnAction(this);
		btn.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));

		return btn;
	}

	@Override
	public void handle(ActionEvent event) {
		Button button = (Button) event.getTarget();
		this.pedal.toogleEffect(idOf(button));
	}

	private int idOf(Button button) {
		for (int i = 0; i < buttons.length; i++)
			if (buttons[i] == button)
				return i;

		return -1;
	}

	@Override
	public void onChange(ChangeMessage<Multistomp> message) {
		//System.out.println(message.causer());

		if (!(message.cause() == CommonCause.SUPER && // Multistomp
			message.nextMessage().cause() == CommonCause.SUPER && // Patch
			message.nextMessage().nextMessage().cause() == CommonCause.EFFECT))
			return;

		Patch patch = (Patch) message.nextMessage().causer();

		List<Effect> effects = patch.effects();
		for (int i = 0; i < effects.size(); i++) {
			Effect effect = effects.get(i);
			
			Platform.runLater(new SetColorOf(effect, i));
		}
	}

	private final class SetColorOf implements Runnable {
		private Effect effect;
		private int id;

		public SetColorOf(Effect effect, int id) {
			this.effect = effect;
			this.id = id;
		}
		
		@Override
		public void run() {
			Color color = effect.hasActived() ? Color.RED : Color.DARKRED;

			buttons[id].setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}