package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.profiles.UserProfile;
import nz.ac.auckland.se206.profiles.UserProfileManager;

public class LeaderboardController {

  @FXML private Label positionText;

  @FXML private Label playerText;

  @FXML private Label winsText;

  @FXML private Label playerOne;

  @FXML private Label playerTwo;

  @FXML private Label playerThree;

  @FXML private Label playerFour;

  @FXML private Label playerFive;

  @FXML private Label positionOne;

  @FXML private Label positionTwo;

  @FXML private Label positionThree;

  @FXML private Label positionFour;

  @FXML private Label positionFive;

  @FXML private Label winsOne;

  @FXML private Label winsTwo;

  @FXML private Label winsThree;

  @FXML private Label winsFour;

  @FXML private Label winsFive;

  public void initialize() {

    ArrayList<UserProfile> leaderboardProfiles = new ArrayList<UserProfile>();

    for (UserProfile userProfile : UserProfileManager.userProfileList) {
      if (userProfile.getWinsCount() != 0) {
        leaderboardProfiles.add(userProfile);
      }
    }

    if (leaderboardProfiles.isEmpty()) {

      positionText.setVisible(false);
      playerText.setVisible(false);
      winsText.setVisible(false);
      positionOne.setVisible(false);
      positionOne.setVisible(false);
      positionTwo.setVisible(false);
      positionThree.setVisible(false);
      positionFour.setVisible(false);
      positionFive.setVisible(false);
      playerTwo.setText("It's quiet here ...");

    } else {

      Integer[] userWins = new Integer[leaderboardProfiles.size()];

      for (int i = 0; i < leaderboardProfiles.size(); i++) {
        userWins[i] = leaderboardProfiles.get(i).getWinsCount();
      }

      Arrays.sort(userWins, Collections.reverseOrder());

      int positions;

      if (userWins.length < 5) {
        positions = userWins.length;
      } else {
        positions = 5;
      }

      UserProfile[] podiumProfiles = new UserProfile[positions];

      for (int i = 0; i < podiumProfiles.length; i++) {
        for (int j = 0; j < leaderboardProfiles.size(); j++) {
          if (leaderboardProfiles.get(j).getWinsCount() == userWins[i]) {
            podiumProfiles[i] = leaderboardProfiles.get(j);
            leaderboardProfiles.remove(j);
            break;
          }
        }
      }
    }
  }

  @FXML
  private void onReturn(ActionEvent event) {
    // Getting the scene information
    Button button = (Button) event.getSource();
    Scene currentScene = button.getScene();

    // Change to main menu page
    try {
      currentScene.setRoot(App.loadFxml("main_menu"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
