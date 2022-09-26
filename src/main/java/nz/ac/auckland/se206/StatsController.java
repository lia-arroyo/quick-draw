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
    statsTitleLabel.setText(currentProfile.getUserName() + "'s Stats");
  }
}
