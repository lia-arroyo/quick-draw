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
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.profiles.UserProfileManager;
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.words.CategorySelector;

/** This class handles all actions on the zen page. */
public class ZenController {
  @FXML private Canvas canvas;

  @FXML private Label chosenWordLabel;

  @FXML private Button switchButton;

  @FXML private Button speechButton;

  @FXML private ColorPicker penColorPicker;

  @FXML private Label predictionList;

  private GraphicsContext graphic;
  private DoodlePrediction model;

  // mouse coordinates
  private double currentX;
  private double currentY;

  private Color penColor;
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

    // Displaying chosen word
    chosenWordLabel.setText(CategorySelector.chosenWord);
    model = new DoodlePrediction();

    // Adding the chosen word to the history of current user
    UserProfileManager.currentProfile.addWordToHistory(CategorySelector.chosenWord);

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

    runCounter();
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
   * This method is responsible for running the timer. The timer is used to notify the user how much
   * time they have left, and it is also used to refresh the model predictions every second.
   */
  private void runCounter() {

    Timer timer = new Timer();

    // Updating the prediction list every second
    timer.scheduleAtFixedRate(
        new TimerTask() {

          public void run() {

            // Every second, the time left label and model prediction is updated
            Platform.runLater(
                () -> {
                  if (!canvasIsEmpty) {
                    try {
                      updatePrediction(timer, getCurrentSnapshot());
                    } catch (TranslateException e) {
                      e.printStackTrace();
                    }
                  }
                });
          }
        },
        0,
        1000);
  }

  /**
   * Get the current snapshot of the canvas.
   *
   * @return The BufferedImage corresponding to the current canvas content.
   */
  private BufferedImage getCurrentSnapshot() {
    final Image snapshot = canvas.snapshot(null, null);
    final BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);

    final BufferedImage imageColor =
        new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

    final Graphics2D graphics = imageColor.createGraphics();

    graphics.drawImage(image, 0, 0, null);

    // To release memory we dispose.
    graphics.dispose();

    return imageColor;
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

  /** This method is called when the "Clear" button is pressed. */
  @FXML
  private void onClear() {
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    canvasIsEmpty = true;

    // Emptying the prediction list as there's nothing on the canvas
    predictionList.setText("");
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
            textToSpeech.speak(
                "You can switch between brush and pen, change the color, or clear the canvas");
            textToSpeech.speak("You have an unlimited amount of time to draw");
            return null;
          }
        };

    Thread speechThread = new Thread(speechTask);
    speechThread.start();
  }

  /** This method is called when the player changes the color on the color picker. */
  @FXML
  private void onChangeColor() {
    penColor = penColorPicker.getValue();
  }

  /**
   * This method will be called when the user clicks on the back button
   *
   * @param event the action event handler result
   */
  @FXML
  private void onGoBack(ActionEvent event) {

    // confirming that the user wants to go back
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle(null);
    alert.setHeaderText(null);
    alert.setContentText(
        String.format(
            "Are you sure you want to leave? You will lose your drawing!",
            UserProfileManager.currentProfile.getUserName()));

    Optional<ButtonType> result = alert.showAndWait();

    if (result.get() == ButtonType.OK) {
      // Getting scene information
      Button button = (Button) event.getSource();
      Scene sceneButtonIsIn = button.getScene();

      // Switching scenes to the canvas page.
      try {
        sceneButtonIsIn.setRoot(App.loadFxml("main_menu"));

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * This method is called when the user clicks on the done button
   *
   * @param event the action event handler result
   * @throws IOException {@inheritDoc}
   */
  @FXML
  private void onDone(ActionEvent event) throws IOException {
    AfterRoundController.END_MESSAGE = "Nice drawing :)";
    saveCurrentSnapshotOnFile();

    /*
     * Getting the snapshot of the current canvas so that the user can see their art
     * after the round and decide if they should save it or not. Also moving the
     * user to the next scene (after_round) as the round finished.
     */
    Platform.runLater(
        () -> {
          try {
            canvas.getScene().setRoot(App.loadFxml("after_round"));
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
  }
}
