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
import nz.ac.auckland.se206.profiles.UserProfile;
import nz.ac.auckland.se206.profiles.UserProfileManager;

public class CreateProfileController {

  @FXML private TextField profileName;

  private Boolean fileEmpty = false;

  /**
   * JavaFX calls this method once the GUI elements are loaded.
   *
   * @throws IOException
   */
  public void initialize() throws IOException {}

  @FXML
  private void onAddProfile(ActionEvent event) throws IOException {

    // getting info from fxml
    String userName = profileName.getText();
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();

    Gson gson = new Gson();

    // Checking if the username is not empty and if the username is not taken.
    if (userName.strip().length() > 0 && !checkUserNameTaken(userName)) {

      // Creating a new object with the given username and profile image index.
      UserProfile user = new UserProfile(userName, 0);

      // Adding user to the profile list
      UserProfileManager.USER_PROFILE_LIST.add(user);

      // Writing to the json file
      FileWriter fw = new FileWriter("./src/main/resources/profiles/profiles.json");
      gson.toJson(UserProfileManager.USER_PROFILE_LIST, fw);
      fw.flush();

      // Set the current user to the profile that was just made.
      UserProfileManager.CURRENT_PROFILE = user;

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
  }

  private Boolean checkUserNameTaken(String userName) throws IOException {
    if (!UserProfileManager.USER_PROFILE_LIST.isEmpty()) {

      for (UserProfile user : UserProfileManager.USER_PROFILE_LIST) {
        if (user.getUserName().equals(userName)) {
          return true;
        }
      }
    }
    return false;
  }
}
