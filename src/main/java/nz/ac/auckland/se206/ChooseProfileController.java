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

    if (UserProfileManager.currentProfileIndex == -1) {
      UserProfileManager.currentProfileIndex = 0;
      this.userNameLabel.setText(UserProfileManager.userProfileList.get(0).getUserName());

    } else {
      this.userNameLabel.setText(
          UserProfileManager.userProfileList
              .get(UserProfileManager.currentProfileIndex)
              .getUserName());
    }

    updateImage();
  }

  /**
   * This method is invoked when the user clicks on the delete button of the select user profile.
   */
  @FXML
  private void onDeleteUser() {
    if (UserProfileManager.userProfileList.size() > 1) {
      UserProfileManager.userProfileList.remove(UserProfileManager.currentProfileIndex);
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

    updateImage();
  }

  /** This method is invoked when the user clicks on the add button to add a new user. */
  @FXML
  private void onAddUser(ActionEvent event) {

    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();

    try {
      sceneButtonIsIn.setRoot(App.loadFxml("create_profile"));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** This method is invoked when the user clicks on the left arrow. */
  @FXML
  private void onGoLeft() {
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

    updateImage();
  }

  /** This method is invoked when the user clicks on the right arrow. */
  @FXML
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

    updateImage();
  }

  @FXML
  private void onSelectProfile(ActionEvent event) {
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();

    try {
      sceneButtonIsIn.setRoot(App.loadFxml("main_menu"));

    } catch (IOException e) {
      e.printStackTrace();
    }

    UserProfileManager.currentProfile =
        UserProfileManager.userProfileList.get(UserProfileManager.currentProfileIndex);
  }

  /** This method is invoked when the profile image needs to be refreshed */
  private void updateImage() {
    int profileIndex =
        UserProfileManager.userProfileList
            .get(UserProfileManager.currentProfileIndex)
            .getProfileIndex();

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
