package nz.ac.auckland.se206;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import nz.ac.auckland.se206.profiles.UserProfile;
import nz.ac.auckland.se206.profiles.UserProfileManager;
import nz.ac.auckland.se206.speech.TextToSpeech;

public class MainMenuController {

  @FXML private Button speechButton;

  @FXML private Button startButton;

  @FXML private ImageView profileImage;

  @FXML private Label greetingLabel;

  @FXML private ImageView emojiImage;

  /**
   * JavaFX calls this method once the GUI elements are loaded.
   *
   * @throws FileNotFoundException
   */
  public void initialize() throws FileNotFoundException {

    // Setting current user to the current profile
    UserProfile currentUser = UserProfileManager.currentProfile;

    // Setting speech button icon
    Image icon = new Image(this.getClass().getResource("/images/sound.png").toString());
    speechButton.setGraphic(new ImageView(icon));

    Image userProfileImage =
        new Image(
            this.getClass()
                .getResource(
                    String.format("/images/profileImages/%d.PNG", currentUser.getProfileIndex()))
                .toString());
    profileImage.setImage(userProfileImage);

    setGreetingText(currentUser.getUserName());
  }

  /**
   * This method changes the scene from the main menu to the waiting screen when the player clicks
   * on the 'Start a new game' button.
   *
   * @param event when the button is pressed
   */
  @FXML
  private void onMoveToWaiting(ActionEvent event) {
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
   * This button leads the user to the statistics page, where they can see their profile stats.
   *
   * @param event the ActionEvent passed by JAVAFX
   */
  @FXML
  public void onSeeStats(ActionEvent event) {
    // getting scene information
    Button button = (Button) event.getSource();
    Scene currentScene = button.getScene();

    // changing to stats page
    try {
      currentScene.setRoot(App.loadFxml("stats"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * This method closes the window when the player clicks on the 'Exit game' Button.
   *
   * @param event when the button is pressed
   */
  @FXML
  private void onExitGame(ActionEvent event) {
    Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    currentStage.close();
  }

  /**
   * This method plays the sound to communicate with the player, when the player clicks the sound
   * button.
   */
  @FXML
  private void onPlaySound() {

    // Making a new thread for playing the sound
    Task<Void> speechTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            TextToSpeech textToSpeech = new TextToSpeech();
            textToSpeech.speak(
                "Quick, draw", "Start a new game, check your stats, or exit the game");

            return null;
          }
        };

    Thread speechThread = new Thread(speechTask);
    speechThread.start();
  }

  @FXML
  private void onSwitchUser(ActionEvent event) {
    Button button = (Button) event.getSource();
    Scene currentScene = button.getScene();

    // Change to choose profile page
    try {
      currentScene.setRoot(App.loadFxml("choose_profile"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void setGreetingText(String username) {
    // Setting greeting text
    List<String> greetingList = Arrays.asList("Welcome,", "Hello", "Hey", "Hi", "What's up");
    Random random = new Random();
    greetingLabel.setText(
        String.format("%s %s", greetingList.get(random.nextInt(greetingList.size())), username));

    // Setting emoji
    int emojiIndex = random.nextInt(8);
    Image emoji =
        new Image(
            this.getClass()
                .getResource(String.format("/images/emojis/%d.png", emojiIndex))
                .toString());
    emojiImage.setImage(emoji);
  }
}
