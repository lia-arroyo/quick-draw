package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.util.StringConverter;
import nz.ac.auckland.se206.difficulty.DifficultyLevel;
import nz.ac.auckland.se206.difficulty.DifficultyLevel.Accuracy;
import nz.ac.auckland.se206.difficulty.DifficultyLevel.Confidence;
import nz.ac.auckland.se206.difficulty.DifficultyLevel.Time;
import nz.ac.auckland.se206.difficulty.DifficultyLevel.Words;
import nz.ac.auckland.se206.profiles.UserProfileManager;
import nz.ac.auckland.se206.util.SoundUtils;

/** This class is to handle any actions on the Settings page. */
public class SettingsController {

  /**
   * This method is for updating the accuracy difficulty of the chosen user.
   *
   * @param accuracyLevel the accuracy level the user chose
   */
  private static void updateUserAccuracyDifficulty(Accuracy accuracyLevel) {
    // Setting the accuracy level of the current user and prompting to save to file
    UserProfileManager.currentProfile.getDifficultyLevel().setAccuracyLevel(accuracyLevel);
    UserProfileManager.saveToFile();
  }

  /**
   * This method is used to update the difficulty of the words as chosen by the user
   *
   * @param wordsLevel the word difficulty the user chose
   */
  private static void updateUserWordsDifficulty(Words wordsLevel) {
    // Setting the word difficulty setting and prompting to save to file
    UserProfileManager.currentProfile.getDifficultyLevel().setWordsLevel(wordsLevel);
    UserProfileManager.saveToFile();
  }

  /**
   * This method is used to update the time difficulty level that the user has chosen.
   *
   * @param timeLevel time difficulty that the user chose
   */
  private static void updateUserTimeDifficulty(Time timeLevel) {
    // Setting the user time difficulty and saving info
    UserProfileManager.currentProfile.getDifficultyLevel().setTimeLevel(timeLevel);
    UserProfileManager.saveToFile();
  }

  /**
   * This method is used to update the user confidence difficulty level that the user chose
   *
   * @param confidenceLevel the confidence difficulty setting that user chose
   */
  private static void updateUserConfidenceDifficulty(Confidence confidenceLevel) {
    // Setting the confidence level
    UserProfileManager.currentProfile.getDifficultyLevel().setConfidenceLevel(confidenceLevel);
    UserProfileManager.saveToFile();
  }

  @FXML private Slider accuracySlider;

  @FXML private Slider wordsSlider;

  @FXML private Slider timeSlider;

  @FXML private Slider confidenceSlider;

  @FXML private CheckBox soundBox;

  /** This method will be called when the Settings page starts up. */
  public void initialize() {

    // Updating sound fx checkbox
    soundBox.setSelected(UserProfileManager.currentProfile.isSoundOn());

    // Rendering the user's previously chosen settings
    String[] currentDifficulties = new String[4];
    currentDifficulties[0] =
        UserProfileManager.currentProfile.getDifficultyLevel().getAccuracyLevel().toString();
    currentDifficulties[1] =
        UserProfileManager.currentProfile.getDifficultyLevel().getWordsLevel().toString();
    currentDifficulties[2] =
        UserProfileManager.currentProfile.getDifficultyLevel().getTimeLevel().toString();
    currentDifficulties[3] =
        UserProfileManager.currentProfile.getDifficultyLevel().getConfidenceLevel().toString();

    // Assigning slider properties to each setting
    Slider[] difficultySliders = new Slider[4];
    difficultySliders[0] = this.accuracySlider;
    difficultySliders[1] = this.wordsSlider;
    difficultySliders[2] = this.timeSlider;
    difficultySliders[3] = this.confidenceSlider;

    // Associating E,M,H, and Master to each slider
    for (int i = 0; i < 4; i++) {
      if (currentDifficulties[i] == "E") {
        difficultySliders[i].setValue((int) 0);
      } else if (currentDifficulties[i] == "M") {
        difficultySliders[i].setValue((int) 1);
      } else if (currentDifficulties[i] == "H") {
        difficultySliders[i].setValue((int) 2);
      } else {
        difficultySliders[i].setValue((int) 3);
      }
    }

    // Labelling each slide property depending on difficulty
    for (int i = 0; i < 4; i++) {
      // Formatting the label
      difficultySliders[i].setLabelFormatter(
          new StringConverter<Double>() {
            @Override
            public String toString(Double number) {
              // Associating each number to a difficulty
              if (number == 0) {
                return "Easy";
              } else if (number == 1) {
                return "Medium";
              } else if (number == 2) {
                return "Hard";
              } else {
                return "Master";
              }
            }

            // Overriding the inherited fromString method
            @Override
            public Double fromString(String string) {
              return null;
            }
          });
    }

    // Setting up the accuracy difficulty
    difficultySliders[0]
        .valueProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Updating each accuracy difficulty setting with each of the four difficulties
                // depending on the new Value
                if ((double) newValue == 0) {
                  updateUserAccuracyDifficulty(DifficultyLevel.Accuracy.E);
                } else if ((double) newValue == 1) {
                  updateUserAccuracyDifficulty(DifficultyLevel.Accuracy.M);
                } else if ((double) newValue == 2) {
                  updateUserAccuracyDifficulty(DifficultyLevel.Accuracy.H);
                } else {
                  updateUserAccuracyDifficulty(DifficultyLevel.Accuracy.MA);
                }
              }
            });

    // Setting up the word difficulty slider
    difficultySliders[1]
        .valueProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Updating the words difficulty level for each of the four word difficulty
                // settings
                // based on the new Value
                if ((double) newValue == 0) {
                  updateUserWordsDifficulty(DifficultyLevel.Words.E);
                } else if ((double) newValue == 1) {
                  updateUserWordsDifficulty(DifficultyLevel.Words.M);
                } else if ((double) newValue == 2) {
                  updateUserWordsDifficulty(DifficultyLevel.Words.H);
                } else {
                  updateUserWordsDifficulty(DifficultyLevel.Words.MA);
                }
              }
            });

    // Setting up time difficulty slider
    difficultySliders[2]
        .valueProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Updating the timer duration depending on which time difficulty setting the
                // user
                // has chosen out of E, M, H and Master
                if ((double) newValue == 0) {
                  updateUserTimeDifficulty(DifficultyLevel.Time.E);
                } else if ((double) newValue == 1) {
                  updateUserTimeDifficulty(DifficultyLevel.Time.M);
                } else if ((double) newValue == 2) {
                  updateUserTimeDifficulty(DifficultyLevel.Time.H);
                } else {
                  updateUserTimeDifficulty(DifficultyLevel.Time.MA);
                }
              }
            });

    // Updating confidence difficulty setting slider
    difficultySliders[3]
        .valueProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Updating the confidence difficulty setting based on the new Value for each of
                // the
                // four difficulty settings: E, M, H, and Master
                if ((double) newValue == 0) {
                  updateUserConfidenceDifficulty(DifficultyLevel.Confidence.E);
                } else if ((double) newValue == 1) {
                  updateUserConfidenceDifficulty(DifficultyLevel.Confidence.M);
                } else if ((double) newValue == 2) {
                  updateUserConfidenceDifficulty(DifficultyLevel.Confidence.H);
                } else {
                  updateUserConfidenceDifficulty(DifficultyLevel.Confidence.MA);
                }
              }
            });
  }

  /**
   * This method is called when the user clicks on the back button.
   *
   * @param event the source of the button click
   */
  @FXML
  private void onGoBack(ActionEvent event) {

    // Saving the sound settings of the user
    UserProfileManager.currentProfile.setSoundOn(soundBox.isSelected());

    // Getting the scene information
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();

    // Changing the scene to waiting screen
    try {
      // Playing the sound associated to all buttons
      SoundUtils soundPlayer = new SoundUtils();
      soundPlayer.playButtonSound();

      // Going back to main menu page
      sceneButtonIsIn.setRoot(App.loadFxml("main_menu"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
