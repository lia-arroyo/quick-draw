package nz.ac.auckland.se206.games;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import nz.ac.auckland.se206.words.CategorySelector;

/** This class is the object for storing game statistics. */
public class Game {
  private String word;
  private CategorySelector.Difficulty wordDifficulty;
  private boolean isWin;
  private String timePlayedFormatted;
  private double accuracy;

  /**
   * This is a constructor for setting the instance variables as stated below:
   *
   * @param isWin the outcome of the game. true if won and false if loss.
   * @param timePlayed the current time that the game is finished
   * @param accuracy the prediction percentage
   */
  public Game(boolean isWin, LocalDateTime timePlayed, double accuracy) {
    this.word = CategorySelector.chosenWord;
    this.wordDifficulty = CategorySelector.currentDifficulty;
    this.isWin = isWin;

    // formatting date and time
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM YYYY hh:mm a");
    this.timePlayedFormatted = timePlayed.format(formatter);

    // only updating accuracy if user has won
    if (accuracy != 0 && isWin) {
      this.accuracy = accuracy;
    }
  }

  /**
   * This is a getter method for the word for each game.
   *
   * @return the game's word
   */
  public String getWord() {
    return word;
  }

  /**
   * This is a getter method for the game's word difficulty category.
   *
   * @return the word difficulty
   */
  public CategorySelector.Difficulty getWordDifficulty() {
    return wordDifficulty;
  }

  /**
   * This is a getter method for the game's result.
   *
   * @return true if won, false if loss.
   */
  public boolean getResult() {
    return isWin;
  }

  /**
   * This getter method is for the time they finished the game.
   *
   * @return the time the user has finished the game, formatted as DD-MM-YYYY HH:MM
   */
  public String getTimePlayed() {
    return this.timePlayedFormatted;
  }

  /**
   * This getter method is for getting the accuracy (prediction percentage) of their drawing.
   *
   * @return the accuracy of their drawing
   */
  public double getAccuracy() {
    return accuracy;
  }
}
