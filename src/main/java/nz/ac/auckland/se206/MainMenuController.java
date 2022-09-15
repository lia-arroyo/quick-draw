package nz.ac.auckland.se206;

import java.io.IOException;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import nz.ac.auckland.se206.speech.TextToSpeech;

public class MainMenuController {

	@FXML
	private Button speechButton;

	/** JavaFX calls this method once the GUI elements are loaded. */
	public void initialize() {

		// Setting speech button icon
		Image icon = new Image(this.getClass().getResource("/images/sound.png").toString());
		speechButton.setGraphic(new ImageView(icon));
	}

	/**
	 * This method changes the scene from the main menu to the waiting screen when
	 * the player clicks on the 'Start a new game' button.
	 *
	 * @param event when the button is pressed
	 */
	@FXML
	private void moveToWaiting(ActionEvent event) {

		Button button = (Button) event.getSource();
		Scene sceneButtonIsIn = button.getScene();

		// Loading the fxml file to change the scene to waiting screen
		try {
			sceneButtonIsIn.setRoot(App.loadFxml("waiting"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method closes the window when the player clicks on the 'Exit game'
	 * Button.
	 *
	 * @param event when the button is pressed
	 */
	@FXML
	private void exitGame(ActionEvent event) {
		Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
		currentStage.close();
	}

	/**
	 * This method plays the sound to communicate with the player, when the player
	 * clicks the sound button.
	 */
	@FXML
	private void playSound() {

		// Making a new thread for playing the sound
		Task<Void> speechTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				TextToSpeech textToSpeech = new TextToSpeech();
				textToSpeech.speak("Quick, draw", "Start a new game or exit the game");

				return null;
			}
		};

		Thread speechThread = new Thread(speechTask);
		speechThread.start();
	}
}
