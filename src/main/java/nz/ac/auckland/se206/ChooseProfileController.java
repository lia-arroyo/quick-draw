package nz.ac.auckland.se206;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.profiles.UserProfileManager;

public class ChooseProfileController {

  @FXML private Button leftButton;

  @FXML private Button rightButton;

  @FXML private Button deleteUserButton;

  @FXML private Button addUserButton;

  @FXML private Button profileImageButton;

  @FXML private Label userNameLabel;

  /**
   * JavaFX calls this method once the GUI elements are loaded.
   *
   * @throws URISyntaxException
   * @throws IOException
   */
  public void initialize() throws IOException, URISyntaxException {

    // The displayed profile is the first profile created by default unless a new
    // profile was just created
    if (UserProfileManager.currentProfileIndex == -1) {
      UserProfileManager.currentProfileIndex = 0;
      this.userNameLabel.setText(UserProfileManager.userProfileList.get(0).getUserName());
    } else {
      this.userNameLabel.setText(
          UserProfileManager.userProfileList
              .get(UserProfileManager.currentProfileIndex)
              .getUserName());
    }

    // Changes the profile image to the one the user chose
    updateImage();
  }

  /**
   * This method is invoked when the user clicks on the delete button of the select user profile.
   */
  @FXML
  private void onDeleteUser() {
    // Can only delete user profile if there are at least current user profiles
    if (UserProfileManager.userProfileList.size() > 1) {
      UserProfileManager.userProfileList.remove(UserProfileManager.currentProfileIndex);
      // When deleted, the next user profile is displayed instead
      if (UserProfileManager.currentProfileIndex == UserProfileManager.userProfileList.size()) {
        UserProfileManager.currentProfileIndex = 0;
        this.userNameLabel.setText(
            UserProfileManager.userProfileList
                .get(UserProfileManager.currentProfileIndex)
                .getUserName());
      } else {
        this.userNameLabel.setText(
            UserProfileManager.userProfileList
                .get(UserProfileManager.currentProfileIndex)
                .getUserName());
      }
    }
    // Changes the profile image to the one the user chose
    updateImage();
  }

  /** This method is invoked when the user clicks on the add button to add a new user. */
  @FXML
  private void onAddUser(ActionEvent event) {

    // Get the button from the event source
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();

    // Loads the scene for profile creation
    try {
      sceneButtonIsIn.setRoot(App.loadFxml("create_profile"));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** This method is invoked when the user clicks on the left arrow. */
  @FXML
  private void onGoLeft() {
    // Displays the next user profile to the left of the current one in the array
    // list unless the profile is the first profile, in which case the last profile
    // is displayed
    if (UserProfileManager.currentProfileIndex == 0) {
      UserProfileManager.currentProfileIndex = UserProfileManager.userProfileList.size() - 1;
      this.userNameLabel.setText(
          UserProfileManager.userProfileList
              .get(UserProfileManager.currentProfileIndex)
              .getUserName());
    } else {
      UserProfileManager.currentProfileIndex--;
      this.userNameLabel.setText(
          UserProfileManager.userProfileList
              .get(UserProfileManager.currentProfileIndex)
              .getUserName());
    }
    // Changes the profile image to the one the user chose
    updateImage();
  }

  /** This method is invoked when the user clicks on the right arrow. */
  @FXML
  // Displays the next user profile to the right of the current one in the array
  // list unless the profile is the last profile, in which case the first profile
  // is displayed
  private void onGoRight() {
    if (UserProfileManager.currentProfileIndex + 1 == UserProfileManager.userProfileList.size()) {
      UserProfileManager.currentProfileIndex = 0;
      this.userNameLabel.setText(
          UserProfileManager.userProfileList
              .get(UserProfileManager.currentProfileIndex)
              .getUserName());
    } else {
      UserProfileManager.currentProfileIndex++;
      this.userNameLabel.setText(
          UserProfileManager.userProfileList
              .get(UserProfileManager.currentProfileIndex)
              .getUserName());
    }
    // Changes the profile image to the one the user chose
    updateImage();
  }

  @FXML
  private void onSelectProfile(ActionEvent event) {
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();

    // Sets the current user profile to the chosen user profile
    UserProfileManager.currentProfile =
        UserProfileManager.userProfileList.get(UserProfileManager.currentProfileIndex);

    // Loads the main menu scene
    try {
      sceneButtonIsIn.setRoot(App.loadFxml("main_menu"));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** This method is invoked when the profile image needs to be refreshed */
  private void updateImage() {
    // Gets the profile index of the current profile
    int profileIndex =
        UserProfileManager.userProfileList
            .get(UserProfileManager.currentProfileIndex)
            .getProfileIndex();

    // Gets the user profile image of the current profile
    Image avatarImage =
        new Image(
            this.getClass()
                .getResource(String.format("/images/profileImages/%d.PNG", profileIndex))
                .toString());

    // Set image width and height to the button's width and height
    ImageView avatarImageView = new ImageView(avatarImage);
    avatarImageView.setFitWidth(profileImageButton.getPrefWidth());
    avatarImageView.setFitHeight(profileImageButton.getPrefHeight());

    // Set button background colour to transparent
    profileImageButton.setStyle("-fx-background-color: transparent;");

    // Set button image to the profile image of the user
    profileImageButton.setGraphic(avatarImageView);
  }
}
