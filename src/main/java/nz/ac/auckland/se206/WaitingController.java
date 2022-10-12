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
import nz.ac.auckland.se206.difficulty.DifficultyLevel;
import nz.ac.auckland.se206.difficulty.DifficultyLevel.Words;
import nz.ac.auckland.se206.profiles.UserProfileManager;
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.words.CategorySelector;
import nz.ac.auckland.se206.words.CategorySelector.Difficulty;

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

    // Getting the difficulty level based on the users' chosen settings.
    Words wordsLevel = UserProfileManager.currentProfile.getDifficultyLevel().getWordsLevel();

    // easy difficulty - selecting easy words only.
    if (wordsLevel == DifficultyLevel.Words.E) {
      categorySelector.setNewChosenWord(Difficulty.E);

      // medium difficulty - easy + medium words
    } else if (wordsLevel == DifficultyLevel.Words.M) {
      double randomNumber = Math.random();

      // 30% easy, 70% medium word
      if (randomNumber < 0.3) {
        categorySelector.setNewChosenWord(Difficulty.E);
      } else {
        categorySelector.setNewChosenWord(Difficulty.M);
      }

      // hard difficulty - easy + medium + hard words
    } else if (wordsLevel == DifficultyLevel.Words.H) {
      double randomNumber = Math.random();

      // 10% easy, 30% medium, 60% hard word
      if (randomNumber < 0.1) {
        categorySelector.setNewChosenWord(Difficulty.E);
      } else if (randomNumber < 0.4) {
        categorySelector.setNewChosenWord(Difficulty.M);
      } else {
        categorySelector.setNewChosenWord(Difficulty.H);
      }

      // master difficulty - hard words only
    } else {
      categorySelector.setNewChosenWord(Difficulty.H);
    }

    // updating label
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

    String currentMode = new String();

    if (MainMenuController.gameMode == 0) {
      currentMode = "canvas";
    } else if (MainMenuController.gameMode == 2) {
      currentMode = "zen";
    }

    // Switching scenes to the canvas page.
    try {
      sceneButtonIsIn.setRoot(App.loadFxml(currentMode));

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
