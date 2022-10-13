package nz.ac.auckland.se206;

import ai.djl.ModelException;
import ai.djl.modality.Classifications;
import ai.djl.translate.TranslateException;
import com.opencsv.exceptions.CsvException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Timer;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import nz.ac.auckland.se206.difficulty.DifficultyLevel;
import nz.ac.auckland.se206.difficulty.DifficultyLevel.Words;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.profiles.UserProfileManager;
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.words.CategorySelector;
import nz.ac.auckland.se206.words.CategorySelector.Difficulty;

public class HiddenWordController {

  @FXML private Canvas canvas;

  @FXML private Button switchButton;

  @FXML private Button readyButton;

  @FXML private Button closeButton;

  @FXML private Button speechButton;

  @FXML private Button definitionButton;

  @FXML private Pane canvasPane;

  @FXML private Pane definitionPane;

  @FXML private Label predictionList;

  @FXML private Label chosenWordLabel;

  @FXML private Label closerFurtherLabel;

  @FXML private ImageView closerFurtherImage;

  private GraphicsContext graphic;
  private DoodlePrediction model;

  // mouse coordinates
  private double currentX;
  private double currentY;

  private Color penColor;
  private Boolean eraser = false;
  private Boolean canvasIsEmpty = true;

  /**
   * JavaFX calls this method once the GUI elements are loaded.
   *
   * @throws IOException
   * @throws ModelException
   * @throws URISyntaxException
   * @throws CsvException
   */
  public void initialize() throws ModelException, IOException, CsvException, URISyntaxException {

    setChosenWordLabel();

    // Setting speech button icon
    Image icon = new Image(this.getClass().getResource("/images/sound.png").toString());
    speechButton.setGraphic(new ImageView(icon));

    model = new DoodlePrediction();

    graphic = canvas.getGraphicsContext2D();

    // save coordinates when mouse is pressed on the canvas
    canvas.setOnMousePressed(
        e -> {
          currentX = e.getX();
          currentY = e.getY();
        });

    canvas.setOnMouseDragged(
        e -> {
          // Brush size (you can change this, it should not be too small or too large).
          final double size = 6.0;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          if (eraser) {
            graphic.setStroke(Color.WHITE);
            graphic.setLineWidth(size * 3);

          } else {
            graphic.setStroke(penColor);
            graphic.setLineWidth(size);
          }

          // Create a line that goes from the point (currentX, currentY) and (x,y)
          graphic.strokeLine(currentX, currentY, x, y);

          // update the coordinates
          currentX = x;
          currentY = y;

          canvasIsEmpty = false;
        });
  }

  private void setChosenWordLabel() throws IOException, CsvException, URISyntaxException {
    // Selecting a random word for user
    CategorySelector categorySelector = new CategorySelector();

    // Getting the difficulty level based on the users' chosen settings.
    Words wordsLevel = UserProfileManager.currentProfile.getDifficultyLevel().getWordsLevel();

    if (wordsLevel == DifficultyLevel.Words.E) {
      // only easy words
      categorySelector.setNewChosenWord(Difficulty.E);

      // medium difficulty - easy + medium words
    } else if (wordsLevel == DifficultyLevel.Words.M) {
      // easy or medium words
      double randomNumber = Math.random();

      // 30% easy, 70% medium word
      if (randomNumber < 0.3) {
        categorySelector.setNewChosenWord(Difficulty.E);
      } else {
        categorySelector.setNewChosenWord(Difficulty.M);
      }

      // hard difficulty - easy + medium + hard words
    } else if (wordsLevel == DifficultyLevel.Words.H) {
      // easy, medium or hard words
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
      // only hard words
      categorySelector.setNewChosenWord(Difficulty.H);
    }

    String underscored = categorySelector.getChosenWord().replaceAll(" ", "  ");
    underscored = underscored.replaceAll("[a-zA-Z]", "_ ");

    // updating the label
    chosenWordLabel.setText(underscored);
  }

  @FXML
  private void onReady() {

    readyButton.setVisible(false);
    closeButton.setVisible(true);

    definitionPane.setVisible(false);
    canvasPane.setVisible(true);
  }

  @FXML
  private void onViewDefinition() {
    canvasPane.setOpacity(0.3);
    definitionPane.setVisible(true);
  }

  @FXML
  private void onCloseDefinition() {

    definitionPane.setVisible(false);
    canvasPane.setOpacity(1);
  }

  /**
   * This method updates the top 10 prediction list when called.
   *
   * @throws TranslateException if there is an error while getting the predictions from the model
   */
  private void updatePrediction(Timer timer, BufferedImage snapshot) throws TranslateException {

    // Making a thread for the prediction functions
    Task<Void> predictionTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {

            // Getting the top 10 predictions according to the current drawing on the canvas
            List<Classifications.Classification> predictions = model.getPredictions(snapshot, 10);
            StringBuilder sb = new StringBuilder();

            // Building the string to set as the prediction label on the GUI
            sb.append("Top 10 Predictions\n\n");

            int i = 1;

            for (final Classifications.Classification classification : predictions) {
              // Removing underscores if they exist in the string
              String categoryName = classification.getClassName().replaceAll("_", " ");

              // Building the string to set as the prediction label
              sb.append(i).append(". ").append(categoryName).append(System.lineSeparator());
              i++;
            }

            // Updating the prediction label in the GUI
            Platform.runLater(
                () -> {
                  predictionList.setText(sb.toString());
                });

            return null;
          }
        };

    Thread predictionThread = new Thread(predictionTask);
    predictionThread.start();
  }

  /**
   * This method is called when the "Clear" button is pressed. It will empty the canvas and also
   * reset the prediction list and the closer/further section.
   */
  @FXML
  private void onClear() {
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    canvasIsEmpty = true;

    // Emptying the prediction list as there's nothing on the canvas
    predictionList.setText("");

    // Emptying the closer/further text and image
    closerFurtherLabel.setText("");
    closerFurtherImage.setImage(null);
  }

  /**
   * This method is called when the user clicks on the switch button. If they are currently using
   * the brush, clicking the button will change the brush to the eraser, and vice versa.
   */
  @FXML
  private void onSwitchBrushEraser() {
    // Once eraser is pressed, button switches to "switch to brush"
    if (eraser == true) {
      eraser = false;
      switchButton.setText("Switch to eraser");

      // Once brush is pressed, button switches to "switch to eraser"
    } else {
      eraser = true;
      switchButton.setText("Switch to brush");
    }
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

            // Telling the user what there word is, and notifying them that they can switch
            // between brush and pen, and also the remaining time.
            textToSpeech.speak("Your word is", CategorySelector.chosenWord);
            textToSpeech.speak("You can switch between brush and pen, or clear the canvas");
            textToSpeech.speak("You have a total of one minute to draw");

            return null;
          }
        };

    Thread speechThread = new Thread(speechTask);
    speechThread.start();
  }
}
