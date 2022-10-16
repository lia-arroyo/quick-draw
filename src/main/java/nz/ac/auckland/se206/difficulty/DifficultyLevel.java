package nz.ac.auckland.se206.difficulty;

import nz.ac.auckland.se206.profiles.UserProfileManager;

/** This class is for all operations regarding the difficulty settings for the user. */
public class DifficultyLevel {

  /** This enum is for the four accuracy levels */
  public enum Accuracy {
    E,
    M,
    H,
    MA
  }

  /** This enum is for the word difficulty settings */
  public enum Words {
    E,
    M,
    H,
    MA
  }

  /** This enum is for the time settings */
  public enum Time {
    E,
    M,
    H,
    MA
  }

  /** This enum is for the confidence (prediction percentage) difficulty settings */
  public enum Confidence {
    E,
    M,
    H,
    MA
  }

  /**
   * This method returns the current accuracy level setting of the user.
   *
   * @return the current accuracy level
   */
  public static int getAccuracyIndex() {
    // Getting the current accuracy level of the current user
    Accuracy accuracyLevel =
        UserProfileManager.currentProfile.getDifficultyLevel().getAccuracyLevel();

    int accuracyIndex;

    // Assigning different accuracy indices depending on the current accuracy
    // difficulty level of
    // the current user
    if (accuracyLevel == DifficultyLevel.Accuracy.E) {
      accuracyIndex = 5;
    } else if (accuracyLevel == DifficultyLevel.Accuracy.M) {
      accuracyIndex = 3;
    } else if (accuracyLevel == DifficultyLevel.Accuracy.H) {
      accuracyIndex = 2;
    } else {
      accuracyIndex = 1;
    }

    return accuracyIndex;
  }

  /**
   * This method returns the current time level setting of the user.
   *
   * @return the current draw time
   */
  public static int getDrawTime() {
    // Getting the current time difficulty level of user
    Time timeLevel = UserProfileManager.currentProfile.getDifficultyLevel().getTimeLevel();

    int drawTime;

    // Assigning drawing time durations depending on the chosen difficulty level
    // setting of the
    // current user
    if (timeLevel == DifficultyLevel.Time.E) {
      drawTime = 60;
    } else if (timeLevel == DifficultyLevel.Time.M) {
      drawTime = 45;
    } else if (timeLevel == DifficultyLevel.Time.H) {
      drawTime = 30;
    } else {
      drawTime = 15;
    }

    return drawTime;
  }

  /**
   * This method returns the current confidence level setting of the user.
   *
   * @return the current prediction confidence
   */
  public static int getPredictionConfidence() {
    // Getting the current confidence level of the current user
    Confidence confidenceLevel =
        UserProfileManager.currentProfile.getDifficultyLevel().getConfidenceLevel();

    int predictionConfidence;

    // Updating prediction confidence depending on the difficulty level that the
    // current user chose
    if (confidenceLevel == DifficultyLevel.Confidence.E) {
      predictionConfidence = 1;
    } else if (confidenceLevel == DifficultyLevel.Confidence.M) {
      predictionConfidence = 10;
    } else if (confidenceLevel == DifficultyLevel.Confidence.H) {
      predictionConfidence = 25;
    } else {
      predictionConfidence = 50;
    }

    // Returning the prediction confidence
    return predictionConfidence;
  }

  private Accuracy accuracyLevel;

  private Words wordsLevel;

  private Time timeLevel;

  private Confidence confidenceLevel;

  /** This constructor is for initiating the different difficulty settings. */
  public DifficultyLevel() {
    // Initialising to EASY level
    this.accuracyLevel = Accuracy.E;
    this.wordsLevel = Words.E;
    this.timeLevel = Time.E;
    this.confidenceLevel = Confidence.E;
  }

  /**
   * This getter method is for the accuracy level that the user has chosen.
   *
   * @return the accuracy level
   */
  public Accuracy getAccuracyLevel() {
    return this.accuracyLevel;
  }

  /**
   * This getter method is for the word difficulty level that the user has chosen.
   *
   * @return the words difficulty
   */
  public Words getWordsLevel() {
    return this.wordsLevel;
  }

  /**
   * This getter method is for the time difficulty level setting.
   *
   * @return the time level
   */
  public Time getTimeLevel() {
    return this.timeLevel;
  }

  /**
   * This getter method is for the confidence level difficulty setting.
   *
   * @return the confidence level
   */
  public Confidence getConfidenceLevel() {
    return this.confidenceLevel;
  }

  /**
   * This method is for setting the accuracy level difficulty setting that is chosen by the user.
   *
   * @param accuracyLevel the accuracy level that the user chose
   */
  public void setAccuracyLevel(Accuracy accuracyLevel) {
    this.accuracyLevel = accuracyLevel;
  }

  /**
   * This method is for setting the word level difficulty setting chosen by the user.
   *
   * @param wordsLevel the word difficulty level that the user chose
   */
  public void setWordsLevel(Words wordsLevel) {
    this.wordsLevel = wordsLevel;
  }

  /**
   * This setter method is for the time difficulty level that the user has chosen.
   *
   * @param timeLevel the time level difficulty
   */
  public void setTimeLevel(Time timeLevel) {
    this.timeLevel = timeLevel;
  }

  /**
   * This setter method is for the confidence level setting that the user has chosen.
   *
   * @param confidenceLevel the confidence level that the user chose.
   */
  public void setConfidenceLevel(Confidence confidenceLevel) {
    this.confidenceLevel = confidenceLevel;
  }
}
