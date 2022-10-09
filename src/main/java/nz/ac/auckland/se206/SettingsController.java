package nz.ac.auckland.se206;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.util.StringConverter;
import nz.ac.auckland.se206.difficulty.DifficultyLevel;
import nz.ac.auckland.se206.difficulty.DifficultyLevel.Accuracy;
import nz.ac.auckland.se206.difficulty.DifficultyLevel.Confidence;
import nz.ac.auckland.se206.difficulty.DifficultyLevel.Time;
import nz.ac.auckland.se206.difficulty.DifficultyLevel.Words;
import nz.ac.auckland.se206.profiles.UserProfileManager;

public class SettingsController {

	@FXML
	private Slider accuracySlider;

	@FXML
	private Slider wordsSlider;

	@FXML
	private Slider timeSlider;

	@FXML
	private Slider confidenceSlider;

	private static void updateUserAccuracyDifficulty(Accuracy accuracyLevel) {
		UserProfileManager.userProfileList.get(UserProfileManager.currentProfileIndex).getDifficultyLevel()
				.setAccuracyLevel(accuracyLevel);
	}

	private static void updateUserWordsDifficulty(Words wordsLevel) {
		UserProfileManager.userProfileList.get(UserProfileManager.currentProfileIndex).getDifficultyLevel()
				.setWordsLevel(wordsLevel);
	}

	private static void updateUserTimeDifficulty(Time timeLevel) {
		UserProfileManager.userProfileList.get(UserProfileManager.currentProfileIndex).getDifficultyLevel()
				.setTimeLevel(timeLevel);
	}

	private static void updateUserConfidenceDifficulty(Confidence confidenceLevel) {
		UserProfileManager.userProfileList.get(UserProfileManager.currentProfileIndex).getDifficultyLevel()
				.setConfidenceLevel(confidenceLevel);
	}

	public void initialize() {
		String[] currentDifficulties = new String[4];
		currentDifficulties[0] = UserProfileManager.userProfileList.get(UserProfileManager.currentProfileIndex)
				.getDifficultyLevel().getAccuracyLevel().toString();
		currentDifficulties[1] = UserProfileManager.userProfileList.get(UserProfileManager.currentProfileIndex)
				.getDifficultyLevel().getWordsLevel().toString();
		currentDifficulties[2] = UserProfileManager.userProfileList.get(UserProfileManager.currentProfileIndex)
				.getDifficultyLevel().getTimeLevel().toString();
		currentDifficulties[3] = UserProfileManager.userProfileList.get(UserProfileManager.currentProfileIndex)
				.getDifficultyLevel().getConfidenceLevel().toString();

		Slider[] difficultySliders = new Slider[4];
		difficultySliders[0] = this.accuracySlider;
		difficultySliders[1] = this.wordsSlider;
		difficultySliders[2] = this.timeSlider;
		difficultySliders[3] = this.confidenceSlider;

		for (int i = 0; i < 4; i++) {
			if (currentDifficulties[i] == "E") {
				difficultySliders[i].setValue((int) 0);
			} else if (currentDifficulties[i] == "M") {
				difficultySliders[i].setValue((int) 1);
			} else if (currentDifficulties[i] == "H") {
				difficultySliders[i].setValue((int) 2);
			}
		}

		for (int i = 0; i < 4; i++) {
			difficultySliders[i].setLabelFormatter(new StringConverter<Double>() {
				@Override
				public String toString(Double number) {
					if (number == 0) {
						return "Easy";
					} else if (number == 1) {
						return "Medium";
					} else {
						return "Hard";
					}
				}

				@Override
				public Double fromString(String string) {
					return null;
				}
			});
		}

		difficultySliders[0].valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if ((int) newValue == 0) {
					updateUserAccuracyDifficulty(DifficultyLevel.Accuracy.E);
				} else if ((int) newValue == 1) {
					updateUserAccuracyDifficulty(DifficultyLevel.Accuracy.M);
				} else {
					updateUserAccuracyDifficulty(DifficultyLevel.Accuracy.H);
				}
			}
		});

		difficultySliders[1].valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if ((int) newValue == 0) {
					updateUserWordsDifficulty(DifficultyLevel.Words.E);
				} else if ((int) newValue == 1) {
					updateUserWordsDifficulty(DifficultyLevel.Words.M);
				} else {
					updateUserWordsDifficulty(DifficultyLevel.Words.H);
				}
			}
		});

		difficultySliders[0].valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if ((int) newValue == 0) {
					updateUserTimeDifficulty(DifficultyLevel.Time.E);
				} else if ((int) newValue == 1) {
					updateUserTimeDifficulty(DifficultyLevel.Time.M);
				} else {
					updateUserTimeDifficulty(DifficultyLevel.Time.H);
				}
			}
		});

		difficultySliders[0].valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if ((int) newValue == 0) {
					updateUserConfidenceDifficulty(DifficultyLevel.Confidence.E);
				} else if ((int) newValue == 1) {
					updateUserConfidenceDifficulty(DifficultyLevel.Confidence.M);
				} else {
					updateUserConfidenceDifficulty(DifficultyLevel.Confidence.H);
				}
			}
		});
	}

}
