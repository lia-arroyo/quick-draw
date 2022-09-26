package nz.ac.auckland.se206;

import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.profiles.UserProfile;
import nz.ac.auckland.se206.profiles.UserProfileManager;

public class CreateProfileController {

  @FXML private TextField profileName;

  @FXML private Button editAvatarButton;

  @FXML private Button addProfileButton;

  @FXML private Pane pane;

  @FXML ImageView avatarImage;

  private Boolean fileEmpty = false;

  private Boolean avatarChosen = false;

  /**
   * JavaFX calls this method once the GUI elements are loaded.
   *
   * @throws IOException
   */
  public void initialize() throws IOException {
    profileName.setText(UserProfileManager.chosenUsername);
    profileName.setFocusTraversable(false);

    Image initialAvatar =
        new Image(
            this.getClass()
                .getResource(
                    String.format(
                        "/images/profileImages/%d.PNG", UserProfileManager.chosenProfileIndex))
                .toString());
    avatarImage.setImage(initialAvatar);
  }

  @FXML
  private void onAddProfile(ActionEvent event) throws IOException {
    // Getting the currently chosen profile image index
    int profileIndex = UserProfileManager.chosenProfileIndex;

    // getting info from fxml
    String userName = profileName.getText();
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();

    Gson gson = new Gson();

    // Checking if the username is not empty and if the username is not taken.
    if (userName.strip().length() > 0 && !checkUserNameTaken(userName)) {

      // Creating a new object with the given username and profile image index.
      UserProfile user = new UserProfile(userName, profileIndex);

      // Adding user to the profile list
      UserProfileManager.userProfileList.add(user);

      // Writing to the json file
      FileWriter fw = new FileWriter("./src/main/resources/profiles/profiles.json");
      gson.toJson(UserProfileManager.userProfileList, fw);
      fw.flush();

      // Set the current user to the profile that was just made.
      UserProfileManager.currentProfileIndex = UserProfileManager.userProfileList.size() - 1;

      // User is redirected to the main menu
      sceneButtonIsIn.setRoot(App.loadFxml("main_menu"));

    } else if (userName.length() > 0) {
      // If the username is already taken, then we alert the user.
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Failed to create profile");
      alert.setHeaderText(null);
      alert.setContentText("Username already exists. Please choose another name.");

      alert.showAndWait();
    }

    // Resetting chosen username string and profile index to default values
    UserProfileManager.chosenUsername = "";
    UserProfileManager.chosenProfileIndex = 1;
  }

  @FXML
  private void onChooseAvatar(ActionEvent event) {
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();

    // Saving current input from the textfield so that when the user comes back to
    // the create profile page, the text field does not reset.
    UserProfileManager.chosenUsername = profileName.getText();

    // Loading the fxml file of the choose avatar page
    try {
      sceneButtonIsIn.setRoot(App.loadFxml("choose_avatar"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private Boolean checkUserNameTaken(String userName) throws IOException {
    if (!UserProfileManager.userProfileList.isEmpty()) {

      for (UserProfile user : UserProfileManager.userProfileList) {
        if (user.getUserName().equals(userName)) {
          return true;
        }
      }
    }
    return false;
  }
}
