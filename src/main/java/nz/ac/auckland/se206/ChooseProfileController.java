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

  @FXML private ImageView profileImage;

  @FXML private Label userNameLabel;

  /**
   * JavaFX calls this method once the GUI elements are loaded.
   *
   * @throws URISyntaxException
   * @throws IOException
   */
  public void initialize() throws IOException, URISyntaxException {

    // Load the selected user image
    Image userProfile =
        new Image(this.getClass().getResource("/images/profileImages/0.jpg").toString());
    profileImage.setImage(userProfile);

    if (UserProfileManager.CURRENT_PRROFILE_INDEX == -1) {
      UserProfileManager.CURRENT_PRROFILE_INDEX = 0;
      this.userNameLabel.setText(UserProfileManager.USER_PROFILE_LIST.get(0).getUserName());
    } else {
      this.userNameLabel.setText(
          UserProfileManager.USER_PROFILE_LIST
              .get(UserProfileManager.CURRENT_PRROFILE_INDEX)
              .getUserName());
    }
  }

  /**
   * This method is invoked when the user clicks on the delete button of the select user profile.
   */
  @FXML
  private void onDeleteUser() {}

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
    System.out.println("Go left");
  }

  /** This method is invoked when the user clicks on the right arrow. */
  @FXML
  private void onGoRight() {
    System.out.println("Go right");
  }
}
