package nz.ac.auckland.se206.profiles;

import java.util.ArrayList;
import nz.ac.auckland.se206.difficulty.DifficultyLevel;
import nz.ac.auckland.se206.games.Game;

public class UserProfile {

  private String userName;
  private int profileIndex;
  private int wins = 0;
  private int losses = 0;
  private ArrayList<String> wordHistory = new ArrayList<>();
  private double highestPredictionPercentage = 0;
  private DifficultyLevel difficultyLevel;
  private ArrayList<Game> historyOfGames = new ArrayList<>();

  /**
   * This is the constructor for the User Profile class to set usernames and such.
   *
   * @param userName name chosen by the user
   * @param profileIndex the avatar index
   */
  public UserProfile(String userName, int profileIndex) {
    this.userName = userName;
    this.profileIndex = profileIndex;
    this.difficultyLevel = new DifficultyLevel();
  }

  /**
   * This getter method is for the name chosen by the user.
   *
   * @return the name chosen by the user.
   */
  public String getUserName() {
    return this.userName;
  }

  /**
   * This getter method is for the index of the avatar
   *
   * @return the index of the chosen avatar
   */
  public int getProfileIndex() {
    return this.profileIndex;
  }

  /**
   * This getter method is for the number of wins that the user has.
   *
   * @return the number of wins
   */
  public int getWinsCount() {
    return this.wins;
  }

  /**
   * This is a getter method for the number of rounds lost by the user
   *
   * @return the number of losses
   */
  public int getLossesCount() {
    return this.losses;
  }

  /** This method will increase the number of wins by 1. It is called when the round is won. */
  public void incrementWinsCount() {
    this.wins++;
  }

  /**
   * This method will increase the number of rounds lost by 1. It is called when the timer is up and
   * the user has not won.
   */
  public void incrementLossesCount() {
    this.losses++;
  }

  // TODO: update the usages of this method to use new history of games arraylist
  public ArrayList<String> getWordHistory() {
    return this.wordHistory;
  }

  /**
   * This method adds to the user's existing word history arraylist.
   *
   * @param word the word the user has played
   */
  // TODO: update usages to use the new history of games list
  public void addWordToHistory(String word) {
    this.wordHistory.add(word);
  }

  /**
   * This is a getter method for the highest prediction percentage of the user.
   *
   * @return the highest prediction percentage
   */
  public double getHighestPrediction() {
    return this.highestPredictionPercentage;
  }

  /**
   * This is a setter method for setting the new highest prediction percentage.
   *
   * @param percentage the new highest prediction percentage
   */
  public void setHighestPredictionPercentage(double percentage) {
    this.highestPredictionPercentage = percentage;
  }

  /**
   * This is a getter method for the difficulty level settings that the user has chosen
   *
   * @return the difficulty level settings
   */
  public DifficultyLevel getDifficultyLevel() {
    return this.difficultyLevel;
  }

  /**
   * This is a getter method for getting the history of games list
   *
   * @return the history of games list
   */
  public ArrayList<Game> getHistoryOfGames() {
    return historyOfGames;
  }

  /**
   * This method will add a game instance with related statistics to the history of games list.
   *
   * @param game the game instance
   */
  public void addGameToHistory(Game game) {
    this.historyOfGames.add(game);
  }
}
