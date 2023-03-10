package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.profiles.UserProfileManager;
import nz.ac.auckland.se206.util.SoundUtils;

/** This class is to handle any action on the Choose Avatar page. */
public class ChooseAvatarController {

  @FXML private VBox avatarBox;

  private SoundUtils soundPlayer;

  /** JavaFX calls this method once the GUI elements are loaded. */
  public void initialize() {

    // Initiate sound player
    soundPlayer = new SoundUtils();

    // Displays the first image as the default image
    int imageIndex = 1;

    for (Node hbox : avatarBox.getChildren()) {
      HBox avatarRow = (HBox) hbox;

      for (Node avatarButton : avatarRow.getChildren()) {

        Button avatar = (Button) avatarButton;

        // Loads each available image to choose for the user profile image
        ImageView avatarImage =
            new ImageView(
                new Image(
                    this.getClass()
                        .getResource(String.format("/images/profileImages/%d.PNG", imageIndex))
                        .toString()));

        // Sets the image dimensions to the required dimensions
        avatarImage.setFitWidth(avatar.getPrefWidth());
        avatarImage.setFitHeight(avatar.getPrefHeight());

        // Sets the available images for display
        avatar.setGraphic(avatarImage);
        avatar.setId(String.format("avatar_%d", imageIndex));

        imageIndex++;
      }
    }
  }

  /**
   * This method is called when the user clicks on the back button.
   *
   * @param event the event handler result
   */
  @FXML
  private void onGoBack(ActionEvent event) {
    // Directing the user back to the create profile page
    goToCreateProfile(event);
  }

  /**
   * This method is called when the user clicks on an avatar.
   *
   * @param event event handler result
   */
  @FXML
  private void onClickImage(ActionEvent event) {
    // getting scene information
    Button avatarButton = (Button) event.getSource();
    String profileIndex = avatarButton.getId().replaceAll("[^0-9]", "");

    // updating the profile index
    UserProfileManager.chosenProfileIndex = Integer.valueOf(profileIndex);

    // Loads the scene for profile creation
    goToCreateProfile(event);
  }

  /**
   * This method is called when the user wants to go to the create profile page.
   *
   * @param event the event handler result
   */
  private void goToCreateProfile(ActionEvent event) {

    // getting scene info
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();

    // Loads the scene for profile creation
    try {
      soundPlayer.playButtonSound();
      sceneButtonIsIn.setRoot(App.loadFxml("create_profile"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
