package nz.ac.auckland.se206;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.util.SoundUtils;
import nz.ac.auckland.se206.words.CategorySelector;

/** This class handles all actions on the waiting page. */
public class WaitingController {

  @FXML private Label chosenWord;

  @FXML private Button speechButton;

  /**
   * JavaFX calls this method once the GUI elements are loaded. As soon as the player enters the
   * waiting screen, a new word is randomly chosen and displayed to the user.
   *
   * @throws IOException if the csv file is not found
   * @throws CsvException all exceptions for opencsv
   * @throws URISyntaxException if a string cannot be used as a URI
   */
  public void initialize() throws IOException, CsvException, URISyntaxException {

    // Setting speech button icon
    Image icon = new Image(this.getClass().getResource("/images/sound.png").toString());
    speechButton.setGraphic(new ImageView(icon));

    // Selecting a random word for user
    CategorySelector categorySelector = new CategorySelector();

    categorySelector.setWordWithDifficulty();

    // updating the label
    chosenWord.setText(categorySelector.getChosenWord());
  }

  /**
   * This method is called when the user clicks the ready button, which will direct the user to the
   * actual game round.
   *
   * @param event when the button is pressed
   */
  @FXML
  private void onStartGame(ActionEvent event) {
    // Getting scene information
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();

    // Switching scenes to the canvas page.
    try {
      SoundUtils soundPlayer = new SoundUtils();
      soundPlayer.playButtonSound();

      sceneButtonIsIn.setRoot(App.loadFxml("canvas"));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method plays the sound to communicate with the player, when the player clicks the sound
   * button.
   */
  @FXML
  private void onPlaySound() {

    // Making a thread for playing the sound
    Task<Void> speechTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {

            // Telling the user their word, and notifying them to press the button when they
            // are ready
            TextToSpeech textToSpeech = new TextToSpeech();
            textToSpeech.speak("Your word is", CategorySelector.chosenWord);
            textToSpeech.speak("Press the button when ready");
            return null;
          }
        };

    // Starting the thread
    Thread speechThread = new Thread(speechTask);
    speechThread.start();
  }
}
