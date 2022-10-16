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
import javafx.scene.shape.Line;
import nz.ac.auckland.se206.profiles.UserProfile;
import nz.ac.auckland.se206.profiles.UserProfileManager;
import nz.ac.auckland.se206.util.SoundUtils;

public class LeaderboardController {

  private enum Statistic {
    BADGES,
    CONFIDENCE,
    WINS
  }

  @FXML private Label positionText;

  @FXML private Label playerText;

  @FXML private Label statsLabel;

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

  @FXML private Line dividerLine;

  @FXML private ChoiceBox<String> leaderboardStatistic;

  private SoundUtils soundPlayer;

  /**
   * This method loads the leaderboard of the stat user wins by default, or displays a message if
   * there is no leaderboard to show
   */
  public void initialize() {
    // Initiate the sound player
    soundPlayer = new SoundUtils();

    // Gets the user profiles that are eligible for leaderboard positions
    ArrayList<UserProfile> leaderboardProfiles = getProfiles(Statistic.WINS);

    // If there are no profiles that are eligible for the leaderboard, display
    // message to indicate that
    if (leaderboardProfiles.isEmpty()) {
      setEmptyScene();

    } else {
      // Gets the user profiles that will be displayed on the leaderboard
      UserProfile[] podiumProfiles = getPodiumProfiles(Statistic.WINS, leaderboardProfiles);

      // Displays the user profiles on the leaderboard page
      setPodium(Statistic.WINS, podiumProfiles);

      // If the current user if on the leaderboard, they are displayed as bold
      setPodiumBold(getLeaderboardIndex(podiumProfiles));

      // Sets the options for the drop down box to chose which leaderboard to display
      leaderboardStatistic.setValue("Wins");
      ObservableList<String> statisticOptions =
          FXCollections.observableArrayList("Badges", "Confidence", "Wins");
      leaderboardStatistic.setItems(statisticOptions);

      // Creates a listener that reacts to a change in the drop down box
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
                  soundPlayer.playButtonSound();

                  if ((int) newValue == 0) {
                    // Gets the user profiles that are eligible for leaderboard positions
                    ArrayList<UserProfile> leaderboardProfiles = getProfiles(Statistic.BADGES);

                    // If there are no profiles that are eligible for the leaderboard, display
                    // message to indicate that
                    if (leaderboardProfiles.isEmpty()) {
                      setEmptyScene();

                    } else {
                      // Displays the user profiles on the leaderboard page
                      setPodiumRegular();
                      UserProfile[] podiumProfiles =
                          getPodiumProfiles(Statistic.BADGES, leaderboardProfiles);
                      setPodium(Statistic.BADGES, podiumProfiles);
                      setPodiumBold(getLeaderboardIndex(podiumProfiles));
                    }

                  } else if ((int) newValue == 1) {
                    // Gets the user profiles that are eligible for leaderboard positions
                    ArrayList<UserProfile> leaderboardProfiles = getProfiles(Statistic.CONFIDENCE);

                    // If there are no profiles that are eligible for the leaderboard, display
                    // message to indicate that
                    if (leaderboardProfiles.isEmpty()) {
                      setEmptyScene();

                    } else {
                      // Displays the user profiles on the leaderboard page
                      setPodiumRegular();
                      UserProfile[] podiumProfiles =
                          getPodiumProfiles(Statistic.CONFIDENCE, leaderboardProfiles);
                      setPodium(Statistic.CONFIDENCE, podiumProfiles);
                      setPodiumBold(getLeaderboardIndex(podiumProfiles));
                    }

                  } else {
                    // Gets the user profiles that are eligible for leaderboard positions
                    ArrayList<UserProfile> leaderboardProfiles = getProfiles(Statistic.WINS);

                    // If there are no profiles that are eligible for the leaderboard, display
                    // message to indicate that
                    if (leaderboardProfiles.isEmpty()) {
                      setEmptyScene();

                    } else {
                      // Displays the user profiles on the leaderboard page
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

  /** This method resets the font style of the leaderboard page to regular font */
  private void setPodiumRegular() {
    // Setting all leaderboard positions to regular font style
    setRegularStyle(positionOne, playerOne, winsOne);
    setRegularStyle(positionTwo, playerTwo, winsTwo);
    setRegularStyle(positionThree, playerThree, winsThree);
    setRegularStyle(positionFour, playerFour, winsFour);
    setRegularStyle(positionFive, playerFive, winsFive);
  }

  /**
   * This method sets the font style of the user name and stats to bold if the user if on the
   * leaderboard
   *
   * @param leaderboardIndex the leader position the user is at
   */
  private void setPodiumBold(int leaderboardIndex) {
    // Only set the leaderboard position to bold if the user is in the podium
    if (leaderboardIndex != -1) {
      if (leaderboardIndex == 0) {
        // Sets the user at index 0 to bold
        setBoldStyle(positionOne, playerOne, winsOne);
      } else if (leaderboardIndex == 1) {
        // Sets the user at index 1 to bold
        setBoldStyle(positionTwo, playerTwo, winsTwo);
      } else if (leaderboardIndex == 2) {
        // Sets the user at index 2 to bold
        setBoldStyle(positionThree, playerThree, winsThree);
      } else if (leaderboardIndex == 3) {
        // Sets the user at index 3 to bold
        setBoldStyle(positionFour, playerFour, winsFour);
      } else {
        // Sets the user at index 4 to bold
        setBoldStyle(positionFive, playerFive, winsFive);
      }
    }
  }

  /**
   * This method gets the leaderboard index the user is at in the podium profiles
   *
   * @param podiumProfiles the profiles that are on the podium
   * @return the index position of the current user
   */
  private int getLeaderboardIndex(UserProfile[] podiumProfiles) {
    // If the current user is not in the podium, then return -1
    int leaderboardIndex = -1;
    // Gets the index of the podium position the user us in
    for (int i = 0; i < podiumProfiles.length; i++) {
      if (UserProfileManager.currentProfile.getUserName().equals(podiumProfiles[i].getUserName())) {
        leaderboardIndex = i;
        break;
      }
    }
    return leaderboardIndex;
  }

  /**
   * This method displays all users that are in podium positions to the leaderboard page
   *
   * @param statistic the stat of the leaderboard
   * @param podiumProfiles the user profiles in podium positions
   */
  private void setPodium(Statistic statistic, UserProfile[] podiumProfiles) {
    // Sets the user name and stats of place 1 in the podium
    positionOne.setText("1");
    playerOne.setText(podiumProfiles[0].getUserName());
    if (podiumProfiles.length > 1) {
      // Sets the user name and stats of place 2 in the podium
      positionTwo.setText("2");
      playerTwo.setText(podiumProfiles[1].getUserName());
    }
    if (podiumProfiles.length > 2) {
      // Sets the user name and stats of place 3 in the podium
      positionThree.setText("3");
      playerThree.setText(podiumProfiles[2].getUserName());
    }
    if (podiumProfiles.length > 3) {
      // Sets the user name and stats of place 4 in the podium
      positionFour.setText("4");
      playerFour.setText(podiumProfiles[3].getUserName());
    }
    if (podiumProfiles.length > 4) {
      // Sets the user name and stats of place 5 in the podium
      positionFive.setText("5");
      playerFive.setText(podiumProfiles[4].getUserName());
    }
    if (statistic == Statistic.WINS) {
      // Changes the leaderboard page to show the stat number of wins
      statsLabel.setText("Number of Wins");
      // Sets the user stats of place 1 in the podium
      winsOne.setText(String.valueOf(podiumProfiles[0].getWinsCount()));
      if (podiumProfiles.length > 1) {
        // Sets the user stats of place 2 in the podium
        winsTwo.setText(String.valueOf(podiumProfiles[1].getWinsCount()));
      }
      if (podiumProfiles.length > 2) {
        // Sets the user stats of place 3 in the podium
        winsThree.setText(String.valueOf(podiumProfiles[2].getWinsCount()));
      }
      if (podiumProfiles.length > 3) {
        // Sets the user stats of place 4 in the podium
        winsFour.setText(String.valueOf(podiumProfiles[3].getWinsCount()));
      }
      if (podiumProfiles.length > 4) {
        // Sets the user stats of place 5 in the podium
        winsFive.setText(String.valueOf(podiumProfiles[4].getWinsCount()));
      }
    } else if (statistic == Statistic.BADGES) {
      // Changes the leaderboard page to show the stat number of badges
      statsLabel.setText("Badges Earned");
      // Sets the user stats of place 1 in the podium
      winsOne.setText(String.valueOf(podiumProfiles[0].getBadgesCount()));
      if (podiumProfiles.length > 1) {
        // Sets the user stats of place 2 in the podium
        winsTwo.setText(String.valueOf(podiumProfiles[1].getBadgesCount()));
      }
      if (podiumProfiles.length > 2) {
        // Sets the user stats of place 3 in the podium
        winsThree.setText(String.valueOf(podiumProfiles[2].getBadgesCount()));
      }
      if (podiumProfiles.length > 3) {
        // Sets the user stats of place 4 in the podium
        winsFour.setText(String.valueOf(podiumProfiles[3].getBadgesCount()));
      }
      if (podiumProfiles.length > 4) {
        // Sets the user stats of place 5 in the podium
        winsFive.setText(String.valueOf(podiumProfiles[4].getBadgesCount()));
      }
    } else {
      // Changes the leaderboard page to show the stat highest prediction
      statsLabel.setText("Highest Prediction");
      // Sets the user stats of place 1 in the podium
      winsOne.setText(String.valueOf(Math.round(podiumProfiles[0].getHighestPrediction())) + "%");
      if (podiumProfiles.length > 1) {
        // Sets the user stats of place 2 in the podium
        winsTwo.setText(String.valueOf(Math.round(podiumProfiles[1].getHighestPrediction())) + "%");
      }
      if (podiumProfiles.length > 2) {
        // Sets the user stats of place 3 in the podium
        winsThree.setText(
            String.valueOf(Math.round(podiumProfiles[2].getHighestPrediction())) + "%");
      }
      if (podiumProfiles.length > 3) {
        // Sets the user stats of place 4 in the podium
        winsFour.setText(
            String.valueOf(Math.round(podiumProfiles[3].getHighestPrediction())) + "%");
      }
      if (podiumProfiles.length > 4) {
        // Sets the user stats of place 5 in the podium
        winsFive.setText(
            String.valueOf(Math.round(podiumProfiles[4].getHighestPrediction())) + "%");
      }
    }
  }

  /**
   * This method gets the user profiles that should be displayed on the leaderboard page
   *
   * @param statistic the stat of the leaderboard
   * @param leaderboardProfiles the user profiles that qualify for leaderboard positions
   * @return the user profiles that are in the podium
   */
  private UserProfile[] getPodiumProfiles(
      Statistic statistic, ArrayList<UserProfile> leaderboardProfiles) {
    if (statistic == Statistic.WINS) {
      // Array that stores the number of wins of the users
      Integer[] userWins = new Integer[leaderboardProfiles.size()];
      // Puts the number of wins in to the array
      for (int i = 0; i < leaderboardProfiles.size(); i++) {
        userWins[i] = leaderboardProfiles.get(i).getWinsCount();
      }
      // Sorts the array, highest number of wins at the beginning
      Arrays.sort(userWins, Collections.reverseOrder());

      // The number of positions in the podium
      int positions;
      // Gets the number of positions in the podium
      if (userWins.length < 5) {
        positions = userWins.length;
      } else {
        positions = 5;
      }

      // Gets the user profiles that are in podium positions
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

      // Returns the profiles in podium positions
      return podiumProfiles;
    } else if (statistic == Statistic.BADGES) {
      // Array that stores the number of badges of the users
      Integer[] userBadges = new Integer[leaderboardProfiles.size()];
      // Puts the number of badges in to the array
      for (int i = 0; i < leaderboardProfiles.size(); i++) {
        userBadges[i] = leaderboardProfiles.get(i).getBadgesCount();
      }
      // Sorts the array, highest number of badges at the beginning
      Arrays.sort(userBadges, Collections.reverseOrder());

      // The number of positions in the podium
      int positions;
      // Gets the number of positions in the podium
      if (userBadges.length < 5) {
        positions = userBadges.length;
      } else {
        positions = 5;
      }

      // Gets the user profiles that are in podium positions
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

      // Returns the profiles in podium positions
      return podiumProfiles;
    } else {
      // Array that stores the highest prediction of the users
      Double[] userHighestPrediction = new Double[leaderboardProfiles.size()];
      // Puts the highest predictions in to the array
      for (int i = 0; i < leaderboardProfiles.size(); i++) {
        userHighestPrediction[i] = leaderboardProfiles.get(i).getHighestPrediction();
      }
      // Sorts the array, highest of the predictions of badges at the beginning
      Arrays.sort(userHighestPrediction, Collections.reverseOrder());

      // The number of positions in the podium
      int positions;
      // Gets the number of positions in the podium
      if (userHighestPrediction.length < 5) {
        positions = userHighestPrediction.length;
      } else {
        positions = 5;
      }

      // Gets the user profiles that are in podium positions
      UserProfile[] podiumProfiles = new UserProfile[positions];
      for (int i = 0; i < podiumProfiles.length; i++) {
        for (int j = 0; j < leaderboardProfiles.size(); j++) {
          if (leaderboardProfiles.get(j).getHighestPrediction() == userHighestPrediction[i]) {
            podiumProfiles[i] = leaderboardProfiles.get(j);
            leaderboardProfiles.remove(j);
            break;
          }
        }
      }

      // Returns the profiles in podium positions
      return podiumProfiles;
    }
  }

  /** This method sets the scene and displays the message if there are no leaderboard to show */
  private void setEmptyScene() {
    // Titles set to invisible
    positionText.setVisible(false);
    playerText.setVisible(false);
    statsLabel.setVisible(false);
    // Positions set to invisible
    positionOne.setVisible(false);
    positionOne.setVisible(false);
    positionTwo.setVisible(false);
    positionThree.setVisible(false);
    positionFour.setVisible(false);
    positionFive.setVisible(false);
    // Drop down box set to invisible
    leaderboardStatistic.setVisible(false);
    // Displays message to show
    playerTwo.setText("It's quiet here ...");
    dividerLine.setVisible(false);
  }

  /**
   * This method gets the user profiles that qualify for leaderboard positions
   *
   * @param statistic the stat of the leaderboard
   * @return the user profiles that qualify for leaderboard positions
   */
  private ArrayList<UserProfile> getProfiles(Statistic statistic) {
    // Sets the array list of user profiles
    ArrayList<UserProfile> leaderboardProfiles = new ArrayList<UserProfile>();
    if (statistic == Statistic.WINS) {
      // If the user has at least one win, they are added to the array list
      for (UserProfile userProfile : UserProfileManager.userProfileList) {
        if (userProfile.getWinsCount() != 0) {
          leaderboardProfiles.add(userProfile);
        }
      }
    } else if (statistic == Statistic.BADGES) {
      // If the user has at least one badge, they are added to the array list
      for (UserProfile userProfile : UserProfileManager.userProfileList) {
        if (userProfile.getBadgesCount() != 0) {
          leaderboardProfiles.add(userProfile);
        }
      }
    } else {
      // If the user has at least one prediction, they are added to the array list
      for (UserProfile userProfile : UserProfileManager.userProfileList) {
        if (userProfile.getHighestPrediction() != 0) {
          leaderboardProfiles.add(userProfile);
        }
      }
    }
    // Returns the array list of user profiles
    return leaderboardProfiles;
  }

  /**
   * This method resets the font style of a particular row in the leaderboard to regular font
   *
   * @param positionText the position of the row in the leaderboard
   * @param playerText the player in the position of the row in the leaderboards
   * @param statsLabel the stat of the player
   */
  private void setRegularStyle(Label positionText, Label playerText, Label statsLabel) {
    // Sets the font style to regular style
    String regularStyle = "-fx-font-weight: normal;";
    positionText.setStyle(regularStyle);
    playerText.setStyle(regularStyle);
    statsLabel.setStyle(regularStyle);
  }

  /**
   * This method sets the font style of a particular row in the leaderboard to bold font
   *
   * @param positionText the position of the row in the leaderboard
   * @param playerText the player in the position of the row in the leaderboards
   * @param statsLabel the stat of the player
   */
  private void setBoldStyle(Label positionText, Label playerText, Label statsLabel) {
    // Sets the font style to bold
    String boldStyle = "-fx-font-weight: bold;";
    positionText.setStyle(boldStyle);
    playerText.setStyle(boldStyle);
    statsLabel.setStyle(boldStyle);
  }

  /**
   * This method returns the game application to the main menu page
   *
   * @param event the button that is pressed to return to main menu
   */
  @FXML
  private void onGoBack(ActionEvent event) {
    // Getting the scene information
    Button button = (Button) event.getSource();
    Scene currentScene = button.getScene();

    // Change to main menu page
    try {
      soundPlayer.playButtonSound();
      currentScene.setRoot(App.loadFxml("main_menu"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
