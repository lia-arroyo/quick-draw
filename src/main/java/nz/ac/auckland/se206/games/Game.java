package nz.ac.auckland.se206.games;

import nz.ac.auckland.se206.words.CategorySelector;

/** This class is the object for storing game statistics. */
public class Game {
  private String word;
  private CategorySelector.Difficulty wordDifficulty;
  private boolean isWin;

  /**
   * This is a constructor for setting the instance variables as stated below:
   *
   * @param word the chosen word for the game
   * @param difficulty the difficulty of the chosen word
   * @param isWin the outcome of the game. true if won and false if loss.
   */
  public Game(String word, CategorySelector.Difficulty difficulty, boolean isWin) {
    this.word = word;
    this.wordDifficulty = difficulty;
    this.isWin = isWin;
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
  public boolean isWin() {
    return isWin;
  }
}
