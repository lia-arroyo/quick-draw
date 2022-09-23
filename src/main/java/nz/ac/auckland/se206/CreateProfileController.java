package nz.ac.auckland.se206;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import nz.ac.auckland.se206.profiles.UserProfile;

public class CreateProfileController {

	@FXML
	private TextField profileName;

	private List<UserProfile> profileList;

	private Boolean fileEmpty = false;

	/**
	 * JavaFX calls this method once the GUI elements are loaded.
	 * 
	 * @throws IOException
	 */
	public void initialize() throws IOException {
		FileReader fr = new FileReader("./src/main/resources/profiles/profiles.json");

		// If the file is empty (no users yet), then we initialise the profileList as a
		// new ArrayList.
		if (fr.read() == -1) {
			profileList = new ArrayList<>();
			fileEmpty = true;
		}

		fr.close();

	}

	@FXML
	private void onAddProfile(ActionEvent event) throws IOException {

		String userName = profileName.getText();

		Button button = (Button) event.getSource();
		Scene sceneButtonIsIn = button.getScene();

		Gson gson = new Gson();

		// Checking if the username is not empty and if the username is not taken.
		if (userName.strip().length() > 0 && !checkUserNameTaken(userName)) {

			// Creating a new object with the given username and profile image index.
			UserProfile user = new UserProfile(userName, 0);

			/*
			 * Creating a exact copy of the original arraylist and setting that as the
			 * profile list because when we retrieve an array from the json file,
			 * profileList becomes a fixed size and hence cannot be modified.
			 */
			profileList = new ArrayList<>(profileList);
			profileList.add(user);

			// Writing to the json file
			FileWriter fw = new FileWriter("./src/main/resources/profiles/profiles.json");
			gson.toJson(profileList, fw);
			fw.flush();

			// Set the current user to the profile that was just made.
			App.CURRENT_USER = userName;

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
		Gson gson = new Gson();
		FileReader fr = new FileReader("./src/main/resources/profiles/profiles.json");

		if (!fileEmpty) {

			profileList = Arrays.asList(gson.fromJson(fr, UserProfile[].class));

			for (UserProfile user : profileList) {
				if (user.getUserName().equals(userName)) {
					return true;
				}
			}

		}

		return false;

	}

}
