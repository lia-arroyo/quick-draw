package nz.ac.auckland.se206;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.util.SoundUtils;

/** This class handles any actions on the Title pages. */
public class TitleController {

  @FXML private Pane pane;

  @FXML private ImageView titleImage;

  private Boolean noProfiles = false;
  private int imageIndex = 0;

  /**
   * This method will be called when the user loads the Title page.
   *
   * @throws IOException {@inheritDoc}
   */
  public void initialize() throws IOException {

    // Playing the background music
    SoundUtils soundPlayer = new SoundUtils();
    soundPlayer.playBackgroundMusic();

    // Linking to the profile
    File profiles = new File("./src/main/resources/profiles/profiles.json");

    // Checks if a user profile file has been generated
    if (!profiles.exists()) {
      profiles.createNewFile();
      noProfiles = true;

      // Checks if the user profile file is empty or not
    } else if (profiles.length() == 0) {
      noProfiles = true;
    }

    // Creates a timer for loading screen animation
    Timer timer = new Timer();

    timer.schedule(
        new TimerTask() {
          @Override
          public void run() {

            // Displays the images for the loading animation
            if (imageIndex < 5) {

              // Loads the current animation image
              Image image =
                  new Image(
                      this.getClass()
                          .getResource(String.format("/images/titleImages/%d.PNG", imageIndex))
                          .toString());

              // Displays the current animation image
              titleImage.setImage(image);

              // Sets the next image of the animation to be displayed
              imageIndex++;

            } else {
              // When the timer is up, load the profile creation page if there is no current
              // profiles, otherwise load the choose profile page
              if (noProfiles) {
                moveToCreateProfile();

              } else {
                moveToChooseProfile();
              }

              // Ending the timer
              timer.cancel();
            }
          }
        },
        0,
        800);
  }

  /** This method handles the transition between title and create profile page. */
  private void moveToCreateProfile() {

    // Loads the scene for profile creation
    try {
      pane.getScene().setRoot(App.loadFxml("create_profile"));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** This method handles the transition between title page and choose profile page. */
  private void moveToChooseProfile() {

    // Loads the scene for choosing profile
    try {
      pane.getScene().setRoot(App.loadFxml("choose_profile"));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
