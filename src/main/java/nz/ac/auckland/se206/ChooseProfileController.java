package nz.ac.auckland.se206;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ChooseProfileController {

	@FXML
	private Button leftButton;

	@FXML
	private Button rightButton;

	@FXML
	private Button deleteUserButton;

	@FXML
	private Button addUserButton;

	@FXML
	private ImageView profileImage;

	/** JavaFX calls this method once the GUI elements are loaded. */
	public void initialize() {

		JSONParser jsonParser = new JSONParser();

		try {
			FileReader reader = new FileReader(this.getClass().getResource("/profiles/profiles.json").toString());

			Object obj = jsonParser.parse(reader);

			JSONArray profileList = (JSONArray) obj;
			System.out.println(profileList);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("JSON file is empty; no user profiles exist");
		}

		// Load the selected user image
		Image userProfile = new Image(this.getClass().getResource("/images/profileImages/user0.jpg").toString());
		profileImage.setImage(userProfile);

	}

	/**
	 * This method is invoked when the user clicks on the delete button of the
	 * select user profile.
	 */
	@FXML
	private void onDeleteUser() {

	}

	/**
	 * This method is invoked when the user clicks on the add button to add a new
	 * user.
	 */
	@FXML
	private void onAddUser() {

	}

	/**
	 * This method is invoked when the user clicks on the left arrow.
	 */
	@FXML
	private void onGoLeft() {
		System.out.println("Go left");
	}

	/**
	 * This method is invoked when the user clicks on the right arrow.
	 */
	@FXML
	private void onGoRight() {
		System.out.println("Go right");

	}
}
