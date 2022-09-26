package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import nz.ac.auckland.se206.profiles.UserProfile;
import nz.ac.auckland.se206.profiles.UserProfileManager;

public class StatsController {
  @FXML private Label statsTitleLabel;
  @FXML private Label winsLabel;
  @FXML private Label lossesLabel;
  @FXML private Label highestPredictionLabel;
  @FXML private Label wordHistoryLabel;
  @FXML private ImageView userProfileImage;
  @FXML private ScrollPane wordHistoryScrollPane;

  private UserProfile currentProfile = UserProfileManager.currentProfile;

  public void initialize() {
    // updating the title with the current profile's name
    statsTitleLabel.setText(currentProfile.getUserName() + "'s Stats");

    // updating the statistics shown
    updateStatistics();
  }

  /**
   * This method will update the current statistics, based on the current user profile, and display
   * it on screen.
   */
  public void updateStatistics() {
    // updating win count
    winsLabel.setText(currentProfile.getWinsCount() + "");

    // updating the highest prediction
    highestPredictionLabel.setText(Math.round(currentProfile.getHighestPrediction() * 100) + "%");

    // updating loss count
    lossesLabel.setText(currentProfile.getLossesCount() + "");

    // updating word history
    displayWordHistory();
  }

  /** This method will display the word history using a String Builder. */
  public void displayWordHistory() {
    StringBuilder builder = new StringBuilder();

    for (String word : currentProfile.getWordHistory()) {
      builder.append(word + "\n");
    }

    Label words = new Label(builder.toString());
    words.setFont(new Font(38));

    wordHistoryScrollPane.setContent(words);
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
