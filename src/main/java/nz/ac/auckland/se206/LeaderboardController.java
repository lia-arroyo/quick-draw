package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.profiles.UserProfile;
import nz.ac.auckland.se206.profiles.UserProfileManager;

public class LeaderboardController {

  private enum Statistic {
    BADGES,
    CONFIDENCE,
    WINS
  }

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

  @FXML private ChoiceBox<String> leaderboardStatistic;

  public void initialize() {
    ArrayList<UserProfile> leaderboardProfiles = getProfiles(Statistic.WINS);

    if (leaderboardProfiles.isEmpty()) {
      setEmptyScene();

    } else {
      UserProfile[] podiumProfiles = getPodiumProfiles(Statistic.WINS, leaderboardProfiles);

      setPodium(Statistic.WINS, podiumProfiles);

      setPodiumBold(getLeaderboardIndex(podiumProfiles));

      leaderboardStatistic.setValue("Wins");
      ObservableList<String> statisticOptions =
          FXCollections.observableArrayList("Badges", "Confidence", "Wins");
      leaderboardStatistic.setItems(statisticOptions);

      leaderboardStatistic
          .getSelectionModel()
          .selectedIndexProperty()
          .addListener(
              new ChangeListener<Number>() {
                @Override
                public void changed(
                    ObservableValue<? extends Number> observable,
                    Number oldValue,
                    Number newValue) {
                  if ((int) newValue == 0) {
                    ArrayList<UserProfile> leaderboardProfiles = getProfiles(Statistic.BADGES);

                    if (leaderboardProfiles.isEmpty()) {
                      setEmptyScene();

                    } else {
                      setPodiumRegular();
                      UserProfile[] podiumProfiles =
                          getPodiumProfiles(Statistic.BADGES, leaderboardProfiles);
                      setPodium(Statistic.BADGES, podiumProfiles);
                      setPodiumBold(getLeaderboardIndex(podiumProfiles));
                    }

                  } else if ((int) newValue == 1) {
                    ArrayList<UserProfile> leaderboardProfiles = getProfiles(Statistic.CONFIDENCE);

                    if (leaderboardProfiles.isEmpty()) {
                      setEmptyScene();

                    } else {
                      setPodiumRegular();
                      UserProfile[] podiumProfiles =
                          getPodiumProfiles(Statistic.CONFIDENCE, leaderboardProfiles);
                      setPodium(Statistic.CONFIDENCE, podiumProfiles);
                      setPodiumBold(getLeaderboardIndex(podiumProfiles));
                    }

                  } else {
                    ArrayList<UserProfile> leaderboardProfiles = getProfiles(Statistic.WINS);

                    if (leaderboardProfiles.isEmpty()) {
                      setEmptyScene();

                    } else {
                      setPodiumRegular();
                      UserProfile[] podiumProfiles =
                          getPodiumProfiles(Statistic.WINS, leaderboardProfiles);
                      setPodium(Statistic.WINS, podiumProfiles);
                      setPodiumBold(getLeaderboardIndex(podiumProfiles));
                    }
                  }
                }
              });
    }
  }

  private void setPodiumRegular() {
    setRegularStyle(positionOne, playerOne, winsOne);
    setRegularStyle(positionTwo, playerTwo, winsTwo);
    setRegularStyle(positionThree, playerThree, winsThree);
    setRegularStyle(positionFour, playerFour, winsFour);
    setRegularStyle(positionFive, playerFive, winsFive);
  }

  private void setPodiumBold(int leaderboardIndex) {
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

  private int getLeaderboardIndex(UserProfile[] podiumProfiles) {
    int leaderboardIndex = -1;
    for (int i = 0; i < podiumProfiles.length; i++) {
      if (UserProfileManager.currentProfile.getUserName().equals(podiumProfiles[i].getUserName())) {
        leaderboardIndex = i;
        break;
      }
    }
    return leaderboardIndex;
  }

  private void setPodium(Statistic statistic, UserProfile[] podiumProfiles) {
    positionOne.setText("1");
    playerOne.setText(podiumProfiles[0].getUserName());
    if (podiumProfiles.length > 1) {
      positionTwo.setText("2");
      playerTwo.setText(podiumProfiles[1].getUserName());
    }
    if (podiumProfiles.length > 2) {
      positionThree.setText("3");
      playerThree.setText(podiumProfiles[2].getUserName());
    }
    if (podiumProfiles.length > 3) {
      positionFour.setText("4");
      playerFour.setText(podiumProfiles[3].getUserName());
    }
    if (podiumProfiles.length > 4) {
      positionFive.setText("5");
      playerFive.setText(podiumProfiles[4].getUserName());
    }
    if (statistic == Statistic.WINS) {
      winsText.setText("Number of Wins");
      winsOne.setText(String.valueOf(podiumProfiles[0].getWinsCount()));
      if (podiumProfiles.length > 1) {
        winsTwo.setText(String.valueOf(podiumProfiles[1].getWinsCount()));
      }
      if (podiumProfiles.length > 2) {
        winsThree.setText(String.valueOf(podiumProfiles[2].getWinsCount()));
      }
      if (podiumProfiles.length > 3) {
        winsFour.setText(String.valueOf(podiumProfiles[3].getWinsCount()));
      }
      if (podiumProfiles.length > 4) {
        winsFive.setText(String.valueOf(podiumProfiles[4].getWinsCount()));
      }
    } else if (statistic == Statistic.BADGES) {
      winsText.setText("Badges Earned");
      winsOne.setText(String.valueOf(podiumProfiles[0].getBadgesCount()));
      if (podiumProfiles.length > 1) {
        winsTwo.setText(String.valueOf(podiumProfiles[1].getBadgesCount()));
      }
      if (podiumProfiles.length > 2) {
        winsThree.setText(String.valueOf(podiumProfiles[2].getBadgesCount()));
      }
      if (podiumProfiles.length > 3) {
        winsFour.setText(String.valueOf(podiumProfiles[3].getBadgesCount()));
      }
      if (podiumProfiles.length > 4) {
        winsFive.setText(String.valueOf(podiumProfiles[4].getBadgesCount()));
      }
    } else {

    }
  }

  private UserProfile[] getPodiumProfiles(
      Statistic statistic, ArrayList<UserProfile> leaderboardProfiles) {
    if (statistic == Statistic.WINS) {
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

      return podiumProfiles;
    } else if (statistic == Statistic.BADGES) {
      Integer[] userBadges = new Integer[leaderboardProfiles.size()];
      for (int i = 0; i < leaderboardProfiles.size(); i++) {
        userBadges[i] = leaderboardProfiles.get(i).getBadgesCount();
      }
      Arrays.sort(userBadges, Collections.reverseOrder());

      int positions;
      if (userBadges.length < 5) {
        positions = userBadges.length;
      } else {
        positions = 5;
      }

      UserProfile[] podiumProfiles = new UserProfile[positions];
      for (int i = 0; i < podiumProfiles.length; i++) {
        for (int j = 0; j < leaderboardProfiles.size(); j++) {
          if (leaderboardProfiles.get(j).getBadgesCount() == userBadges[i]) {
            podiumProfiles[i] = leaderboardProfiles.get(j);
            leaderboardProfiles.remove(j);
            break;
          }
        }
      }

      return podiumProfiles;
    } else {
      return null;
    }
  }

  private void setEmptyScene() {
    positionText.setVisible(false);
    playerText.setVisible(false);
    winsText.setVisible(false);
    positionOne.setVisible(false);
    positionOne.setVisible(false);
    positionTwo.setVisible(false);
    positionThree.setVisible(false);
    positionFour.setVisible(false);
    positionFive.setVisible(false);
    leaderboardStatistic.setVisible(false);
    playerTwo.setText("It's quiet here ...");
  }

  private ArrayList<UserProfile> getProfiles(Statistic statistic) {
    ArrayList<UserProfile> leaderboardProfiles = new ArrayList<UserProfile>();
    if (statistic == Statistic.WINS) {
      for (UserProfile userProfile : UserProfileManager.userProfileList) {
        if (userProfile.getWinsCount() != 0) {
          leaderboardProfiles.add(userProfile);
        }
      }
    } else if (statistic == Statistic.BADGES) {
      for (UserProfile userProfile : UserProfileManager.userProfileList) {
        if (userProfile.getBadgesCount() != 0) {
          leaderboardProfiles.add(userProfile);
        }
      }
    } else {

    }
    return leaderboardProfiles;
  }

  private void setRegularStyle(Label positionText, Label playerText, Label winsText) {
    String regularStyle = "-fx-font-weight: normal;";
    positionText.setStyle(regularStyle);
    playerText.setStyle(regularStyle);
    winsText.setStyle(regularStyle);
  }

  private void setBoldStyle(Label positionText, Label playerText, Label winsText) {
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
