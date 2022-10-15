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
import java.time.LocalDateTime;
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
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;
import nz.ac.auckland.se206.badges.BadgesManager;
import nz.ac.auckland.se206.difficulty.DifficultyLevel;
import nz.ac.auckland.se206.games.Game;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.profiles.UserProfileManager;
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.words.CategorySelector;
import nz.ac.auckland.se206.words.Dictionary;

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

  @FXML private Label zenChosenWordLabel;

  @FXML private Label timeLeft;

  @FXML private Label predictionList;

  @FXML private Label underscoreLabel;

  @FXML private Label definitionLabel;

  @FXML private Label instructionLabel;

  @FXML private Label closerFurtherLabel;

  @FXML private ImageView closerFurtherImage;

  @FXML private Button switchButton;

  @FXML private Button speechButton;

  @FXML private Button zenSpeechButton;

  @FXML private Button readyButton;

  @FXML private Button definitionButton;

  @FXML private Button revealButton;

  @FXML private Button closeButton;

  @FXML private Pane canvasPane;

  @FXML private Pane definitionPane;

  @FXML private Pane zenTopPane;

  @FXML private HBox timerTopBox;

  @FXML private VBox underscoreBox;

  @FXML private HBox normalBottomBox;

  @FXML private HBox zenBottomBox;

  @FXML private ColorPicker penColorPicker;

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

  private Color penColor = Color.BLACK;
  private Boolean eraser = false;
  private Boolean canvasIsEmpty = true;

  private int predictionIndex = 340;

  private int gameMode = 0;

  /**
   * JavaFX calls this method once the GUI elements are loaded. For the canvas controller, we create
   * a listener for the drawing, and we load the ML model.
   *
   * @throws ModelException If there is an error in reading the input/output of the DL model.
   * @throws IOException If the model cannot be found on the file system.
   */
  public void initialize() throws ModelException, IOException, CsvException, URISyntaxException {

    // Getting the current game mode and game variables
    gameMode = MainMenuController.gameMode;

    accuracyIndex = DifficultyLevel.getAccuracyIndex();

    drawTime = DifficultyLevel.getDrawTime();

    predictionConfidence = DifficultyLevel.getPredictionConfidence();

    model = new DoodlePrediction();

    graphic = canvas.getGraphicsContext2D();

    // Saving coordinates when mouse is pressed on the canvas
    canvas.setOnMousePressed(
        e -> {
          currentX = e.getX();
          currentY = e.getY();
        });

    canvas.setOnMouseDragged(
        e -> {
          // This is the brush size
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

          // Creating a line that goes from the point (currentX, currentY) and (x,y)
          graphic.strokeLine(currentX, currentY, x, y);

          // Updating the coordinates
          currentX = x;
          currentY = y;

          canvasIsEmpty = false;
        });

    setupScreen();
  }

  /**
   * This method is called in the initialize method of the canvas controller to set up the screen
   * initially, according to the game mode.
   *
   * @throws URISyntaxException
   * @throws CsvException
   * @throws IOException
   */
  private void setupScreen() throws IOException, CsvException, URISyntaxException {
    // Setting the speech button icon to the speech image
    Image icon = new Image(this.getClass().getResource("/images/sound.png").toString());
    speechButton.setGraphic(new ImageView(icon));
    zenSpeechButton.setGraphic(new ImageView(icon));

    // When game mode is 'NORMAL'
    if (gameMode == 0) {
      // Displaying the chosen word to the user
      chosenWordLabel.setText(CategorySelector.chosenWord);
      runCounter();

      // When game mode is 'HIDDEN WORD'
    } else if (gameMode == 1) {
      // Disable the chosen word label and canvas pane, then show the definition pane.
      chosenWordLabel.setVisible(false);
      canvasPane.setVisible(false);
      underscoreBox.setVisible(true);
      definitionPane.setVisible(true);

      // Selecting a random word for user
      CategorySelector categorySelector = new CategorySelector();
      categorySelector.setWordWithDifficulty();

      // Printing the word for presentation purposes
      System.out.println(CategorySelector.chosenWord);

      updateDefinitionLabel();

      // When game mode is 'ZEN'
    } else if (gameMode == 2) {

      zenTopPane.setVisible(true);
      timerTopBox.setVisible(false);

      zenBottomBox.setVisible(true);
      normalBottomBox.setVisible(false);

      // Displaying the chosen word to the user
      zenChosenWordLabel.setText(CategorySelector.chosenWord);

      runCounter();
    }
  }

  /**
   * This method is called when the user clicks on the I'm ready button when the definition is first
   * displayed on the screen. It closes the definition pane, shows the canvas, and starts the
   * counter just like in normal mode.
   */
  @FXML
  private void onReady() {
    // Close the definition pane and show the canvas pane
    definitionPane.setVisible(false);
    canvasPane.setVisible(true);

    // Also now the instruction label and ready button won't be visible as it is not
    // needed anymore
    readyButton.setVisible(false);
    instructionLabel.setVisible(false);

    // Now the definition pane can be closed by the close button
    closeButton.setVisible(true);

    underscoreBox.setVisible(true);

    runCounter();
  }

  /**
   * This method is called when the user clicks on the see definition button in the canvas. The
   * definition pane pops up until the user closes it again by pressing on the x button.
   */
  @FXML
  private void onViewDefinition() {
    canvasPane.setOpacity(0.3);
    definitionPane.setVisible(true);
  }

  /**
   * This method is called when the user clicks on the reveal first letter button. This will change
   * the first letter of the underscore label from '_' to whatever the first letter of the chosen
   * word is, and the button will then be disabled.
   */
  @FXML
  private void onRevealFirstLetter() {
    String underscore = underscoreLabel.getText();

    String revealed = CategorySelector.chosenWord.charAt(0) + underscore.substring(1);

    underscoreLabel.setText(revealed);

    revealButton.setDisable(true);
  }

  /**
   * This method is called when the user clicks on the x button. The definition pane closes and the
   * canvas is shown to the user.
   */
  @FXML
  private void onCloseDefinition() {
    definitionPane.setVisible(false);
    canvasPane.setOpacity(1);
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
   * This method gets the current snapshot of the canvas.
   *
   * @return The BufferedImage corresponding to the current canvas content.
   */
  private BufferedImage getCurrentSnapshot() {
    final Image snapshot = canvas.snapshot(null, null);
    final BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);

    // Converting into a color image.
    final BufferedImage buffered =
        new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

    final Graphics2D graphics = buffered.createGraphics();

    graphics.drawImage(image, 0, 0, null);

    // To release memory we dispose.
    graphics.dispose();

    return buffered;
  }

  /**
   * This method saves the current snapshot on a bitmap file.
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
   * time they have left, and it is also used to refresh the model predictions and the
   * closer/further section every second.
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
          private int seconds = secondsLeft + 1;

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
            // the round with 0 (lost). If the game mode is 'ZEN' mode, then we don't have
            // to cancel the round even if the timer reaches 0 seconds.
            if (seconds < 0 && gameMode != 2) {
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
   *
   * @param timeLeft the amount of time left in seconds
   */
  private void updateCounter(int timeLeft) {
    if (timeLeft >= 0) {
      this.timeLeft.setText(String.format("%d", timeLeft));
    }
  }

  /**
   * This method updates the closer or further label / emoji depending on the prediction list. If
   * the user's drawing is getting closer to the top 10, then we display 'closer', if it goes
   * further away then we display 'further'.
   *
   * @throws TranslateException when an error occurs during the processing of an input or output
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

  /**
   * This method returns the current prediction index of the chosen word in the prediction list.
   *
   * @return the current prediction index of the chosen word
   * @throws TranslateException when an error occurs during the processing of an input or output
   */
  private int getPredictionIndex() throws TranslateException {
    List<Classifications.Classification> predictions =
        model.getPredictions(getCurrentSnapshot(), 340);

    String chosenWord = CategorySelector.chosenWord;

    int currentIndex = 0;

    // Going through the whole prediction list to look for the index of the chosen
    // word
    for (Classifications.Classification category : predictions) {
      if (category.getClassName().equals(chosenWord)) {
        return currentIndex;
      }

      currentIndex++;
    }

    // If for some reason the word is not found, we return the size of the
    // prediction list.
    return predictions.size();
  }

  /**
   * This method is called when the definition needs to get displayed for the user. It creates a
   * separate thread to do the API calling to retrieve the definition string, then changes the
   * labels accordingly and sets the 'ready' button to visible.
   */
  private void updateDefinitionLabel() {
    // Making a thread for initialising the dictionary and finding the definition
    Task<Void> updateTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {

            CategorySelector categorySelector = new CategorySelector();

            Dictionary dictionary = new Dictionary(CategorySelector.chosenWord);

            while (!dictionary.getDefinitionExists()) {
              // Selecting a random word for user again as definition does not exist
              categorySelector.setWordWithDifficulty();
              dictionary = new Dictionary(CategorySelector.chosenWord);
              System.out.println(
                  "Got a new word because the definition does not exist. The new word is: "
                      + CategorySelector.chosenWord);
            }

            String definition = dictionary.getDefinition();

            String underscored =
                categorySelector.getChosenWord().replaceAll("-", "  ").replaceAll("[a-zA-Z]", "_ ");

            // Updating the labels in the GUI and setting the ready button to visible so
            // that the user can press it after everything has been prepared.
            Platform.runLater(
                () -> {
                  definitionLabel.setText(definition);

                  underscoreLabel.setText(underscored);

                  readyButton.setVisible(true);
                });

            return null;
          }
        };

    Thread predictionThread = new Thread(updateTask);
    predictionThread.start();
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

            // Only checking for top n predictions if the current game mode is not 'ZEN'
            // mode.
            if (gameMode != 2) {
              // Checking if the top n predictions match the chosen word. If it does, then the
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
      // This is when the user has lost
      AfterRoundController.END_MESSAGE = "You ran out of time  :(";
      UserProfileManager.currentProfile.incrementLossesCount();
      UserProfileManager.currentProfile.resetConsecutiveWins();

    } else if (result == 1) {
      // This is when the user has won
      AfterRoundController.END_MESSAGE = "Congratulations! You won  :)";
      UserProfileManager.currentProfile.incrementWinsCount();
      UserProfileManager.currentProfile.incrementConsecutiveWins();
    }

    // check to see if this game qualifies for badges
    BadgesManager.checkForBadges(gameTime, drawTime, gameConfidence);

    // Saving game statistics
    UserProfileManager.currentProfile.addGameToHistory(
        new Game((result == 1), LocalDateTime.now(), gameConfidence));

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
  private void onSwitchBrushEraser(ActionEvent event) {

    Button button = (Button) event.getSource();

    // Once eraser is pressed, button switches to "switch to brush"
    if (eraser == true) {
      eraser = false;
      button.setText("Switch to eraser");

      // Once brush is pressed, button switches to "switch to eraser"
    } else {
      eraser = true;
      button.setText("Switch to brush");
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

  /**
   * This method is called when the player changes the color on the color picker. The penColor
   * variable is updated.
   */
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

    // Applying style to the alert dialog
    DialogPane dialogPane = alert.getDialogPane();
    dialogPane.getStylesheets().add(this.getClass().getResource("/css/dialog.css").toString());

    alert.setTitle(null);
    alert.setHeaderText(null);
    alert.setContentText("Are you sure you want to leave? You will lose your drawing!");
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
    AfterRoundController.END_MESSAGE = "Nice drawing :P";
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
