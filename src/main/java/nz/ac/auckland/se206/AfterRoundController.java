package nz.ac.auckland.se206;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.util.SoundUtils;
import nz.ac.auckland.se206.words.CategorySelector;

public class AfterRoundController {

  public static String END_MESSAGE = "";

  @FXML private Label displayLabel;

  @FXML private Label chosenWord;

  @FXML private ImageView playerImage;

  @FXML private Button speechButton;

  private SoundUtils soundPlayer;

  private Image image;

  /**
   * JavaFX calls this method once the GUI elements are loaded. We first set the display label to
   * the end message, which is dependent on the previous game scenario. Then we update other
   * components such as labels and the image view.
   *
   * @throws FileNotFoundException if the file cannot be found in the directory
   */
  public void initialize() throws FileNotFoundException {

    // Setting speech button icon to the speech image
    Image icon = new Image(this.getClass().getResource("/images/sound.png").toString());
    speechButton.setGraphic(new ImageView(icon));

    // Displaying the end message and also the chosen word to the user
    displayLabel.setText(END_MESSAGE);
    chosenWord.setText(CategorySelector.chosenWord);

    // Also displaying the image that the user drew on the canvas
    File file = new File(System.getProperty("user.dir") + "/tmp/userImage.bmp/");
    image = new Image(file.toURI().toString());
    playerImage.setImage(image);

    // Initiate sound player
    soundPlayer = new SoundUtils();

    // Turning off intense drawing music after normal and hidden game mode
    if (MainMenuController.gameMode != 2) {
      soundPlayer.stopDrawingMusic();
    }
  }

  /**
   * This method is called when the player clicks on the 'Save image' Button. The drawing from the
   * canvas is saved to the folder that the user chooses through the DirectoryChooser. The drawing
   * is saved as a png file.
   *
   * @param event when the button is pressed
   * @throws IOException if the image cannot be saved in the file system
   */
  @FXML
  private void onSaveImage(ActionEvent event) throws IOException {
    soundPlayer.playButtonSound();

    // Getting current stage information
    Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    BufferedImage buffImage = SwingFXUtils.fromFXImage(image, null);

    FileChooser fileChooser = new FileChooser();

    /*
     * This invokes the user to choose a folder to save their image in. The name of
     * the image is automatically chosen depending on what word they had.
     */
    try {
      // User chooses directory to save their image
      File selectedDirectory =
          new File(fileChooser.showSaveDialog(currentStage).toString() + ".png");
      // Saves image to chosen directory
      ImageIO.write(buffImage, "PNG", selectedDirectory);

    } catch (NullPointerException e) {
      // If the user decides not to save the file, the console notifies us.
      System.err.println("User did not save the file");
    }
  }

  /**
   * This method is called when the player clicks on the restart button. The user is directed back
   * to the main menu.
   *
   * @param event the source of the button click
   */
  @FXML
  private void onRestartGame(ActionEvent event) {
    soundPlayer.playButtonSound();

    // Getting the scene information from the button
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();

    // Directing the user back to the main menu
    try {
      sceneButtonIsIn.setRoot(App.loadFxml("main_menu"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method plays the sound to communicate with the player when the player clicks the sound
   * button.
   */
  @FXML
  private void onPlaySound() {

    // Creating a background task to play text-to-speech
    Task<Void> speechTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            /*
             * Setting the speech message to the end message and also notifying the user
             * that they can save their image or restart the game.
             */
            TextToSpeech textToSpeech = new TextToSpeech();
            textToSpeech.speak(END_MESSAGE);
            textToSpeech.speak("You can save your image or restart the game");
            return null;
          }
        };

    // Creating a thread to run the background task
    Thread speechThread = new Thread(speechTask);
    speechThread.start();
  }
}
