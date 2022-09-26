package nz.ac.auckland.se206;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import nz.ac.auckland.se206.profiles.UserProfile;
import nz.ac.auckland.se206.profiles.UserProfileManager;
import nz.ac.auckland.se206.words.CategorySelector;

public class StatsController {
  @FXML private Label statsTitleLabel;
  @FXML private Label winsLabel;
  @FXML private Label lossesLabel;
  @FXML private Label highestPredictionLabel;
  @FXML private Label progressLabel;
  @FXML private ImageView userProfileImage;
  @FXML private ScrollPane wordHistoryScrollPane;
  @FXML private ProgressBar wordProgressBar;

  private UserProfile currentProfile = UserProfileManager.currentProfile;

  public void initialize() {
    // updating the title with the current profile's name
    statsTitleLabel.setText(currentProfile.getUserName() + "'s Stats");

    // updating the image to be the user's chosen avatar
    Image userAvatar =
        new Image(
            this.getClass()
                .getResource(
                    String.format("/images/profileImages/%d.PNG", currentProfile.getProfileIndex()))
                .toString());
    userProfileImage.setImage(userAvatar);

    // updating the statistics shown
    updateStatistics();
  }

  /**
   * This method will update the current statistics, based on the current user profile, and display
   * it on screen.
   */
  public void updateStatistics() {
    // updating win and loss counts
    winsLabel.setText(currentProfile.getWinsCount() + "");
    lossesLabel.setText(currentProfile.getLossesCount() + "");

    // updating the highest prediction
    highestPredictionLabel.setText(Math.round(currentProfile.getHighestPrediction() * 100) + "%");

    // updating the word history and progress
    displayWordHistory();
    updateProgress();
  }

  /** This method will display the word history using a String Builder. */
  public void displayWordHistory() {
    StringBuilder builder = new StringBuilder();

    // listing every word that the user has played.
    for (String word : currentProfile.getWordHistory()) {
      builder.append(word + "\n");
    }

    // creating new label that resizes depending on length of above string
    Label words = new Label(builder.toString());
    words.setFont(new Font(38));

    // adding to the scroll pane
    wordHistoryScrollPane.setContent(words);
  }

  public void updateProgress() {
    // Creating new category selector to get the number of total words
    CategorySelector categorySelector = null;
    try {
      categorySelector = new CategorySelector();
    } catch (IOException | RuntimeException | URISyntaxException | CsvException e) {
      throw new RuntimeException(e);
    }

    // calculating progress
    int wordsPlayed = currentProfile.getWordHistory().size();
    int wordsInTotal = categorySelector.getTotalWordCount(CategorySelector.Difficulty.E);
    double progress = (double) wordsPlayed / wordsInTotal;

    // updating the progress bar
    wordProgressBar.setProgress(progress);

    // updating the text that goes with it
    progressLabel.setText(
        "You've played a total of "
            + wordsPlayed
            + " words, and only have "
            + (wordsInTotal - wordsPlayed)
            + " words to go! Well done!");
  }

  /**
   * This method is called when the user presses the Go Back button on the Stats page.
   *
   * @param event the ActionEvent handler
   */
  @FXML
  public void onGoBack(ActionEvent event) {
    // getting scene information
    Button button = (Button) event.getSource();
    Scene currentScene = button.getScene();

    // going back to main menu page
    try {
      currentScene.setRoot(App.loadFxml("main_menu"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
