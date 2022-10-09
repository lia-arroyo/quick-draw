package nz.ac.auckland.se206;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.util.StringConverter;
import nz.ac.auckland.se206.difficulty.DifficultyLevel;

public class SettingsController {

	@FXML
	private Slider accuracySlider;

	@FXML
	private Slider wordsSlider;

	@FXML
	private Slider timeSlider;

	@FXML
	private Slider confidenceSlider;

	public void initialize() {
		String[] currentDifficulties = new String[4];
		currentDifficulties[0] = DifficultyLevel.currentAccuracyLevel.toString();
		currentDifficulties[1] = DifficultyLevel.currentWordsLevel.toString();
		currentDifficulties[2] = DifficultyLevel.currentTimeLevel.toString();
		currentDifficulties[3] = DifficultyLevel.currentConfidenceLevel.toString();

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
					DifficultyLevel.currentAccuracyLevel = DifficultyLevel.Accuracy.E;
				} else if ((int) newValue == 1) {
					DifficultyLevel.currentAccuracyLevel = DifficultyLevel.Accuracy.M;
				} else {
					DifficultyLevel.currentAccuracyLevel = DifficultyLevel.Accuracy.H;
				}
			}
		});

		difficultySliders[1].valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if ((int) newValue == 0) {
					DifficultyLevel.currentWordsLevel = DifficultyLevel.Words.E;
				} else if ((int) newValue == 1) {
					DifficultyLevel.currentWordsLevel = DifficultyLevel.Words.M;
				} else {
					DifficultyLevel.currentWordsLevel = DifficultyLevel.Words.H;
				}
			}
		});

		difficultySliders[0].valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if ((int) newValue == 0) {
					DifficultyLevel.currentTimeLevel = DifficultyLevel.Time.E;
				} else if ((int) newValue == 1) {
					DifficultyLevel.currentTimeLevel = DifficultyLevel.Time.M;
				} else {
					DifficultyLevel.currentTimeLevel = DifficultyLevel.Time.H;
				}
			}
		});

		difficultySliders[0].valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if ((int) newValue == 0) {
					DifficultyLevel.currentConfidenceLevel = DifficultyLevel.Confidence.E;
				} else if ((int) newValue == 1) {
					DifficultyLevel.currentConfidenceLevel = DifficultyLevel.Confidence.M;
				} else {
					DifficultyLevel.currentConfidenceLevel = DifficultyLevel.Confidence.H;
				}
			}
		});
	}

}
