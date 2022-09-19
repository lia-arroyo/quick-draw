package nz.ac.auckland.se206;

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
