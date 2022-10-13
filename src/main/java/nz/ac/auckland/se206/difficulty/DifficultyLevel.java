package nz.ac.auckland.se206.difficulty;

/** This class is for all operations regarding the difficulty settings for the user. */
public class DifficultyLevel {

  public enum Accuracy {
    E,
    M,
    H,
    MA
  }

  public enum Words {
    E,
    M,
    H,
    MA
  }

  public enum Time {
    E,
    M,
    H,
    MA
  }

  public enum Confidence {
    E,
    M,
    H,
    MA
  }

  private Accuracy accuracyLevel;

  private Words wordsLevel;

  private Time timeLevel;

  private Confidence confidenceLevel;

  /** This constructor is for initiating the different difficulty settings. */
  public DifficultyLevel() {
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
