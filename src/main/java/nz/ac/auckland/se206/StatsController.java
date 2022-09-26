package nz.ac.auckland.se206;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.profiles.UserProfile;
import nz.ac.auckland.se206.profiles.UserProfileManager;

public class StatsController {
  @FXML private Label statsTitleLabel;
  @FXML private Label winsLabel;
  @FXML private Label lossesLabel;
  @FXML private Label highestPredictionLabel;
  @FXML private ImageView userProfileImage;
  @FXML private Accordion wordsAccordion;

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
  }
}
