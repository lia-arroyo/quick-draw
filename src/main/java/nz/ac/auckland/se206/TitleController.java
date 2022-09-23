package nz.ac.auckland.se206;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class TitleController {

	@FXML
	private Pane pane;

	@FXML
	private ImageView titleImage;

	private Boolean noProfiles = false;
	private int imageIndex = 0;

	public void initialize() throws IOException {

		File profiles = new File("./src/main/resources/profiles/profiles.json");

		if (!profiles.exists()) {
			profiles.createNewFile();
			noProfiles = true;

		} else {
			if (profiles.length() == 0) {
				noProfiles = true;
			}
		}

		Timer timer = new Timer();

		timer.schedule(new TimerTask() {
			@Override
			public void run() {

				if (imageIndex < 5) {

					Image image = new Image(this.getClass()
							.getResource(String.format("/images/titleImages/%d.PNG", imageIndex)).toString());
					titleImage.setImage(image);
					imageIndex++;
				} else {

					if (noProfiles) {
						moveToCreateProfile();

					} else {
						moveToChooseProfile();
					}

					timer.cancel();

				}

			}
		}, 0, 800);

	}

	private void moveToCreateProfile() {
		try {
			pane.getScene().setRoot(App.loadFxml("create_profile"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void moveToChooseProfile() {
		try {
			pane.getScene().setRoot(App.loadFxml("choose_profile"));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
