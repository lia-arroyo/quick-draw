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

      positionOne.setText("1");
      playerOne.setText(podiumProfiles[0].getUserName());
      winsOne.setText(String.valueOf(podiumProfiles[0].getWinsCount()));

      if (podiumProfiles.length > 1) {
        positionTwo.setText("2");
        playerTwo.setText(podiumProfiles[1].getUserName());
        winsTwo.setText(String.valueOf(podiumProfiles[1].getWinsCount()));
      }
      if (podiumProfiles.length > 2) {
        positionThree.setText("3");
        playerThree.setText(podiumProfiles[2].getUserName());
        winsThree.setText(String.valueOf(podiumProfiles[2].getWinsCount()));
      }
      if (podiumProfiles.length > 3) {
        positionFour.setText("4");
        playerFour.setText(podiumProfiles[3].getUserName());
        winsFour.setText(String.valueOf(podiumProfiles[3].getWinsCount()));
      }
      if (podiumProfiles.length > 4) {
        positionFive.setText("5");
        playerFive.setText(podiumProfiles[4].getUserName());
        winsFive.setText(String.valueOf(podiumProfiles[4].getWinsCount()));
      }

      int leaderboardIndex = -1;

      for (int i = 0; i < podiumProfiles.length; i++) {
        if (UserProfileManager.currentProfile
            .getUserName()
            .equals(podiumProfiles[i].getUserName())) {
          leaderboardIndex = i;
          break;
        }
      }

      if (leaderboardIndex != -1) {
        if (leaderboardIndex == 0) {
          setBoldStyle(positionOne, playerOne, winsOne);
        } else if (leaderboardIndex == 1) {
          setBoldStyle(positionTwo, playerTwo, winsTwo);
        } else if (leaderboardIndex == 2) {
          setBoldStyle(positionThree, playerThree, winsThree);
        } else if (leaderboardIndex == 3) {
          setBoldStyle(positionFour, playerFour, winsFour);
        } else {
          setBoldStyle(positionFive, playerFive, winsFive);
        }
      }
    }
  }

  private static void setBoldStyle(Label positionText, Label playerText, Label winsText) {
    String boldStyle = "-fx-font-weight: bold;";
    positionText.setStyle(boldStyle);
    playerText.setStyle(boldStyle);
    winsText.setStyle(boldStyle);
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
