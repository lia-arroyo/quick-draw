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
import nz.ac.auckland.se206.difficulty.DifficultyLevel;
import nz.ac.auckland.se206.difficulty.DifficultyLevel.Accuracy;
import nz.ac.auckland.se206.difficulty.DifficultyLevel.Confidence;
import nz.ac.auckland.se206.difficulty.DifficultyLevel.Time;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.profiles.UserProfileManager;
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

  @FXML private Canvas canvas;

  @FXML private Label chosenWordLabel;

  @FXML private Label timeLeft;

  @FXML private Label predictionList;

  @FXML private Button switchButton;

  @FXML private Button speechButton;

  @FXML private Label closerFurtherLabel;

  @FXML private ImageView closerFurtherImage;

  private GraphicsContext graphic;

  private DoodlePrediction model;

  private int drawTime;

  private int accuracyIndex;

  private int predictionConfidence;

  private int gameTime;

  private double gameConfidence;

  // mouse coordinates
  private double currentX;
  private double currentY;

  private Boolean eraser = false;
  private Boolean canvasIsEmpty = true;

  private int predictionIndex = 340;

  /**
   * JavaFX calls this method once the GUI elements are loaded. In our case we create a listener for
   * the drawing, and we load the ML model.
   *
   * @throws ModelException If there is an error in reading the input/output of the DL model.
   * @throws IOException If the model cannot be found on the file system.
   */
  public void initialize() throws ModelException, IOException, CsvException, URISyntaxException {

    Accuracy accuracyLevel =
        UserProfileManager.userProfileList
            .get(UserProfileManager.currentProfileIndex)
            .getDifficultyLevel()
            .getAccuracyLevel();
    if (accuracyLevel == DifficultyLevel.Accuracy.E) {
      this.accuracyIndex = 5;
    } else if (accuracyLevel == DifficultyLevel.Accuracy.M) {
      this.accuracyIndex = 3;
    } else if (accuracyLevel == DifficultyLevel.Accuracy.H) {
      this.accuracyIndex = 2;
    } else {
      this.accuracyIndex = 1;
    }

    Time timeLevel =
        UserProfileManager.userProfileList
            .get(UserProfileManager.currentProfileIndex)
            .getDifficultyLevel()
            .getTimeLevel();
    if (timeLevel == DifficultyLevel.Time.E) {
      this.drawTime = 60;
    } else if (timeLevel == DifficultyLevel.Time.M) {
      this.drawTime = 45;
    } else if (timeLevel == DifficultyLevel.Time.H) {
      this.drawTime = 30;
    } else {
      this.drawTime = 15;
    }

    Confidence confidenceLevel =
        UserProfileManager.userProfileList
            .get(UserProfileManager.currentProfileIndex)
            .getDifficultyLevel()
            .getConfidenceLevel();
    if (confidenceLevel == DifficultyLevel.Confidence.E) {
      this.predictionConfidence = 1;
    } else if (confidenceLevel == DifficultyLevel.Confidence.M) {
      this.predictionConfidence = 10;
    } else if (confidenceLevel == DifficultyLevel.Confidence.H) {
      this.predictionConfidence = 25;
    } else {
      this.predictionConfidence = 50;
    }

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

    // Emptying the closer/further text and image
    closerFurtherLabel.setText("");
    closerFurtherImage.setImage(null);
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

    int secondsLeft = this.drawTime;
    this.gameTime = -1;

    Timer timer = new Timer();

    // Decrementing variable seconds every second and updating the time left label.
    // If seconds goes
    // below
    // 0, then we finish the round, notifying the player that they ran out of time.
    timer.scheduleAtFixedRate(
        new TimerTask() {
          int seconds = secondsLeft + 1;

          public void run() {

            // Every second, the time left label and model prediction is updated
            Platform.runLater(
                () -> {
                  updateCounter(seconds);
                  if (!canvasIsEmpty) {
                    try {
                      updatePrediction(timer, getCurrentSnapshot());
                      updateCloserFurther();
                    } catch (TranslateException e) {
                      e.printStackTrace();
                    }
                  }
                });
            seconds--;
            gameTime++;

            // If the timer reaches 0, we cancel the timer so that it stops, and we finish
            // the round with 0 (lost)
            if (seconds < 0) {
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
      this.timeLeft.setText(String.format("%d", timeLeft));
    } else {
      // Displays if there is no time left
      this.timeLeft.setText("Time up");
    }
  }

  /**
   * This method updates the closer or further label / emoji depending on the prediction list. If
   * the user's drawing is getting closer to the top 10, then we display 'closer', if it goes
   * further away then we display 'further'.
   *
   * @throws TranslateException
   */
  private void updateCloserFurther() throws TranslateException {

    int previousIndex = predictionIndex;
    predictionIndex = getPredictionIndex();

    if (predictionIndex < 10) {
      closerFurtherLabel.setText("TOP 10!");
      closerFurtherImage.setImage(
          new Image(this.getClass().getResource("/images/emojis/3.png").toString()));
    } else {
      // Closer
      if (predictionIndex < previousIndex) {
        closerFurtherLabel.setText("CLOSER");
        closerFurtherImage.setImage(
            new Image(this.getClass().getResource("/images/emojis/0.png").toString()));

        // Further
      } else if (predictionIndex > previousIndex) {
        closerFurtherLabel.setText("FURTHER");
        closerFurtherImage.setImage(
            new Image(this.getClass().getResource("/images/emojis/8.png").toString()));
      }
    }
  }

  private int getPredictionIndex() throws TranslateException {
    List<Classifications.Classification> predictions =
        model.getPredictions(getCurrentSnapshot(), 340);

    String chosenWord = CategorySelector.chosenWord;

    int currentIndex = 0;

    for (Classifications.Classification category : predictions) {
      if (category.getClassName().equals(chosenWord)) {
        return currentIndex;
      }

      currentIndex++;
    }

    return 340;
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

            // Checking if the top 3 predictions match the chosen word. If it does, then the
            // round finishes with 1 (win) and we stop the timer.
            for (int j = 0; j < accuracyIndex; j++) {
              // Removing underscores if they exist in the string
              String categoryName = predictions.get(j).getClassName().replaceAll("_", " ");

              double predictionPercentage = predictions.get(j).getProbability() * 100;

              if (categoryName.equals(CategorySelector.chosenWord)
                  && !canvasIsEmpty
                  && predictionPercentage >= predictionConfidence) {

                // Checking if prediction is the new highest prediction percentage
                if (predictionPercentage
                    > UserProfileManager.currentProfile.getHighestPrediction()) {
                  UserProfileManager.currentProfile.setHighestPredictionPercentage(
                      predictionPercentage);
                }

                gameConfidence = predictionPercentage;

                // Finishing the round
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
      UserProfileManager.currentProfile.incrementLossesCount();

    } else if (result == 1) {
      // the user has won
      AfterRoundController.END_MESSAGE = "Congratulations! You won  :)";
      UserProfileManager.currentProfile.incrementWinsCount();

      if (gameTime <= 30) {
        UserProfileManager.currentProfile.getBadges()[0] = true;
      }
      if (gameTime <= 15) {
        UserProfileManager.currentProfile.getBadges()[1] = true;
      }
      if (gameTime <= 5) {
        UserProfileManager.currentProfile.getBadges()[2] = true;
      }
      if ((gameTime == drawTime) || (gameTime == (drawTime - 1))) {
        UserProfileManager.currentProfile.getBadges()[3] = true;
      }
      if (gameConfidence >= 75) {
        UserProfileManager.currentProfile.getBadges()[3] = true;
      }
    }

    if (UserProfileManager.currentProfile.getWordHistory().size() == 200) {
      UserProfileManager.currentProfile.getBadges()[4] = true;
    }

    // Ensuring that statistics are saved to file after each round.
    UserProfileManager.saveToFile();

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
