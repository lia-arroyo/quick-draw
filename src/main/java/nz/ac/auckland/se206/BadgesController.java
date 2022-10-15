package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.profiles.UserProfile;
import nz.ac.auckland.se206.profiles.UserProfileManager;

public class BadgesController {

  @FXML private VBox badgeBox;

  private List<String> badgeTitleList;
  private List<String> obtainedBadgeDetailList;
  private List<String> badgeDetailList;

  private UserProfile currentUser;

  /** JavaFX calls this method once the GUI elements are loaded. */
  public void initialize() {

    // Once the user enters the badges page, there will be no more new badges to see
    UserProfileManager.currentProfile.setHasNewBadge(false);

    // Setting the messages for the badge titles and badge details.
    badgeTitleList =
        Arrays.asList(
            "FAST DRAWER",
            "INSANELY FAST DRAWER",
            "SPEEDY SKETCHER!",
            "THE LAST SECOND...",
            "THE GOLDEN PENCIL :0",
            "ON FIRE!!!",
            "ADDICTED?",
            "TOO EZ :)");

    obtainedBadgeDetailList =
        Arrays.asList(
            "Won a game under 30 seconds!",
            "Won a game under 15 seconds!",
            "Won a game under 5 seconds!",
            "Won in the last 5 seconds!",
            "Got over 75% accuracy in a word!",
            "Had a 3 game winning streak!",
            "Had a 10 game winning streak!",
            "Completed all easy words!");

    badgeDetailList =
        Arrays.asList(
            "Win a game in under 30 seconds to obtain this badge.",
            "Win a game in under 15 seconds to obtain this badge.",
            "Win a game in under 5 seconds to obtain this badge.",
            "Win a game in the last 5 seconds to obtain this badge.",
            "Get over 75% accuracy for a word to obtain this badge.",
            "Win 3 times in a row to obtain this badge.",
            "Win 10 times in a row to obtain this badge.",
            "Complete all easy mode words to obtain this badge.");

    int badgeIndex = 0;

    currentUser = UserProfileManager.currentProfile;

    // Going through the VBox to access all the badge buttons within the VBox
    for (Node hbox : badgeBox.getChildren()) {

      HBox badgeRow = (HBox) hbox;

      for (Node badge : badgeRow.getChildren()) {
        Button badgeButton = (Button) badge;

        // Getting the image of the badge depending on the current badge index
        ImageView badgeImageView =
            new ImageView(
                new Image(
                    this.getClass()
                        .getResource(String.format("/images/badges/%d.png", badgeIndex))
                        .toString()));

        badgeImageView.setFitWidth(badgeButton.getPrefWidth());
        badgeImageView.setFitHeight(badgeButton.getPrefHeight());

        // If the player has not obtained the badge yet, the opacity will be set to 30%
        // so that the user knows that they haven't obtained it yet.
        if (!currentUser.getBadges()[badgeIndex]) {
          badgeImageView.setOpacity(0.3);
        }

        badgeButton.setGraphic(badgeImageView);

        // Setting a FX id for the badge button for later use
        badgeButton.setId(String.format("badge_%d", badgeIndex));

        badgeIndex++;
      }
    }
  }

  /**
   * This method is called when the user clicks on the back button on the badges page. The user is
   * directed back to the main menu page.
   *
   * @param event the source of the button click
   */
  @FXML
  private void onGoBack(ActionEvent event) {
    // Getting the scene information from the button
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();

    // Directing the user back to the main menu
    try {
      sceneButtonIsIn.setRoot(App.loadFxml("main_menu"));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method is called when the user clicks on a badge to see the details. The details are shown
   * in alert dialog format.
   *
   * @param event the source of the button click
   */
  @FXML
  private void onShowBadgeDetails(ActionEvent event) {

    Button badge = (Button) event.getSource();

    // Getting the badge id from the clicked badge button
    String badgeId = badge.getId().replaceAll("[^0-9]", "");
    int id = Integer.parseInt(badgeId);

    Alert badgeDetail = new Alert(AlertType.INFORMATION);

    // Applying style to the alert dialog
    DialogPane dialogPane = badgeDetail.getDialogPane();
    dialogPane.getStylesheets().add(this.getClass().getResource("/css/dialog.css").toString());

    // Setting the text for the alert dialog
    badgeDetail.setTitle(null);
    badgeDetail.setHeaderText(badgeTitleList.get(id));

    // Checking if the user has the badge or not so that the explanation changes
    // accordingly
    if (currentUser.getBadges()[id]) {
      badgeDetail.setContentText(obtainedBadgeDetailList.get(id));
    } else {
      badgeDetail.setContentText(badgeDetailList.get(id));
    }

    // Getting the image to set as the icon on the alert dialog
    ImageView badgeImage =
        new ImageView(
            new Image(
                this.getClass()
                    .getResource(String.format("/images/badges/%d.png", id))
                    .toString()));

    badgeImage.setFitWidth(70);
    badgeImage.setFitHeight(70);

    badgeDetail.setGraphic(badgeImage);

    badgeDetail.showAndWait();
  }
}
