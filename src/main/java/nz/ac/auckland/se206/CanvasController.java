package nz.ac.auckland.se206;

import ai.djl.ModelException;
import ai.djl.modality.Classifications;
import ai.djl.translate.TranslateException;
import com.opencsv.exceptions.CsvException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.profiles.UserProfile;
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.words.CategorySelector;

/**
 * This is the controller of the canvas. You are free to modify this class and the corresponding
 * FXML file as you see fit. For example, you might no longer need the "Predict" button because the
 * DL model should be automatically queried in the background every second.
 *
 * <p>!! IMPORTANT !!
 *
 * <p>Although we added the scale of the image, you need to be careful when changing the size of the
 * drawable canvas and the brush size. If you make the brush too big or too small with respect to
 * the canvas size, the ML model will not work correctly. So be careful. If you make some changes in
 * the canvas and brush sizes, make sure that the prediction works fine.
 */
public class CanvasController {

  private static UserProfile currentProfile =
      App.userProfileList.get(
          0); // by default, the current profile is the first one. use setter method to change

  /**
   * Setter method for current profile. Use this when wanting to select the current profile.
   *
   * @param profile the current profile we are working with.
   */
  public static void setCurrentProfile(UserProfile profile) {
    currentProfile = profile;
  }

  @FXML private Canvas canvas;

  @FXML private Label chosenWord;

  @FXML private Label timeLeft;

  @FXML private Label predictionList;

  @FXML private Button switchButton;

  @FXML private Button speechButton;

  private GraphicsContext graphic;

  private DoodlePrediction model;

  // mouse coordinates
  private double currentX;
  private double currentY;

  private Boolean eraser = false;
  private Boolean canvasIsEmpty = true;

  /**
   * JavaFX calls this method once the GUI elements are loaded. In our case we create a listener for
   * the drawing, and we load the ML model.
   *
   * @throws ModelException If there is an error in reading the input/output of the DL model.
   * @throws IOException If the model cannot be found on the file system.
   */
  public void initialize() throws ModelException, IOException, CsvException, URISyntaxException {

    // Setting speech button icon
    Image icon = new Image(this.getClass().getResource("/images/sound.png").toString());
    speechButton.setGraphic(new ImageView(icon));

    chosenWord.setText("[ " + CategorySelector.CHOSEN_WORD + " ]");
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
            graphic.setStroke(Color.BLACK);
            graphic.setLineWidth(size);
          }

          // Create a line that goes from the point (currentX, currentY) and (x,y)
          graphic.strokeLine(currentX, currentY, x, y);

          // update the coordinates
          currentX = x;
          currentY = y;

          canvasIsEmpty = false;
        });

    runCounter();
  }

  /** This method is called when the "Clear" button is pressed. */
  @FXML
  private void onClear() {
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    canvasIsEmpty = true;

    // Emptying the prediction list as there's nothing on the canvas
    predictionList.setText("");
  }

  /**
   * Get the current snapshot of the canvas.
   *
   * @return The BufferedImage corresponding to the current canvas content.
   */
  private BufferedImage getCurrentSnapshot() {
    final Image snapshot = canvas.snapshot(null, null);
    final BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);

    // Convert into a binary image.
    final BufferedImage imageBinary =
        new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

    final Graphics2D graphics = imageBinary.createGraphics();

    graphics.drawImage(image, 0, 0, null);

    // To release memory we dispose.
    graphics.dispose();

    return imageBinary;
  }

  /**
   * Save the current snapshot on a bitmap file.
   *
   * @return The file of the saved image.
   * @throws IOException If the image cannot be saved.
   */
  private File saveCurrentSnapshotOnFile() throws IOException {
    final File tmpFolder = new File("tmp");

    if (!tmpFolder.exists()) {
      tmpFolder.mkdir();
    }

    final File imageToClassify = new File(tmpFolder.getName() + "/userImage" + ".bmp");

    // Save the image to a file.
    ImageIO.write(getCurrentSnapshot(), "bmp", imageToClassify);

    return imageToClassify;
  }

  /**
   * This method is responsible for running the timer. The timer is used to notify the user how much
   * time they have left, and it is also used to refresh the model predictions every second.
   */
  private void runCounter() {

    int secondsLeft = 60;

    Timer timer = new Timer();

    // Decrementing i every second and updating the time left label. If i goes below
    // 0, then we finish the round, notifying the player that they ran out of time.
    timer.scheduleAtFixedRate(
        new TimerTask() {
          int i = secondsLeft + 1;

          public void run() {

            // Every second, the time left label and model prediction is updated
            Platform.runLater(
                () -> {
                  updateCounter(i);
                  if (!canvasIsEmpty) {
                    try {
                      updatePrediction(timer, getCurrentSnapshot());
                    } catch (TranslateException e) {
                      e.printStackTrace();
                    }
                  }
                });
            i--;

            // If the timer reaches 0, we cancel the timer so that it stops, and we finish
            // the round with 0 (lost)
            if (i < 0) {
              timer.cancel();
              finishRound(0);
            }
          }
        },
        0,
        1000);
  }

  /**
   * This method updates the counter shown to the user by receiving the current time left as input.
   * When the time reaches 0, the text shows "Time up".
   *
   * @param timeLeft the amount of time left in seconds
   */
  private void updateCounter(int timeLeft) {
    if (timeLeft >= 0) {
      this.timeLeft.setText(String.format("%d s", timeLeft));
    } else {
      this.timeLeft.setText("Time up");
    }
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
            sb.append("TOP 10 PREDICTIONS\n\n");

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

            // Checking if the top 3 predictions match the chosen word. If it does, then the
            // round finishes with 1 (win) and we stop the timer.
            for (int j = 0; j < 3; j++) {
              // Removing underscores if they exist in the string
              String categoryName = predictions.get(j).getClassName().replaceAll("_", " ");

              if (categoryName.equals(CategorySelector.CHOSEN_WORD) && !canvasIsEmpty) {
                finishRound(1);
                timer.cancel();
              }
            }
            return null;
          }
        };

    Thread predictionThread = new Thread(predictionTask);
    predictionThread.start();
  }

  /**
   * This method is called when the round ends, either by winning or losing (running out of time).
   *
   * @param result if the player wins, result = 1, if the player loses, result = 0
   */
  private void finishRound(int result) {

    // Setting the message label that will be shown to the user depending on the
    // result
    if (result == 0) {
      // the user has lost
      AfterRoundController.END_MESSAGE = "You ran out of time  :(";
      currentProfile.incrementLossesCount();

    } else if (result == 1) {
      // the user has won
      AfterRoundController.END_MESSAGE = "Congratulations! You won  :)";
      currentProfile.incrementWinsCount();
    }

    /*
     * Getting the snapshot of the current canvas so that the user can see their art
     * after the round and decide if they should save it or not. Also moving the
     * user to the next scene (after_round) as the round finished.
     */
    Platform.runLater(
        () -> {
          try {
            saveCurrentSnapshotOnFile();
            canvas.getScene().setRoot(App.loadFxml("after_round"));
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
  }

  /**
   * This method is called when the user clicks on the switch button. If they are currently using
   * the brush, clicking the button will change the brush to the eraser, and vice versa.
   */
  @FXML
  private void onSwitchBrushEraser() {
    if (eraser == true) {
      eraser = false;
      switchButton.setText("Switch to eraser");

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
            textToSpeech.speak("Your word is", CategorySelector.CHOSEN_WORD);
            textToSpeech.speak("You can switch between brush and pen, or clear the canvas");
            textToSpeech.speak("You have a total of one minute to draw");

            return null;
          }
        };

    Thread speechThread = new Thread(speechTask);
    speechThread.start();
  }
}
