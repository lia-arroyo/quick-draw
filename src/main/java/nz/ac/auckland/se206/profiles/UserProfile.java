package nz.ac.auckland.se206.profiles;

import java.util.ArrayList;
import nz.ac.auckland.se206.difficulty.DifficultyLevel;
import nz.ac.auckland.se206.games.Game;
import nz.ac.auckland.se206.words.CategorySelector;

public class UserProfile {

  private String userName;
  private int profileIndex;
  private int wins = 0;
  private int losses = 0;
  private double highestPredictionPercentage = 0;
  private DifficultyLevel difficultyLevel;
  private ArrayList<Game> historyOfGames = new ArrayList<>();
  private boolean[] badges = new boolean[8];
  private int consecutiveWins = 0;
  private Boolean soundOn = true;

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

  /**
   * This method takes the history of games list and grabs only the words and nothing else.
   *
   * @return an arraylist containing all the played words
   */
  public ArrayList<String> getWordHistory() {
    ArrayList<String> wordHistory = new ArrayList<>();

    // iterating through each game and adding word to the list
    historyOfGames.forEach(
        game -> {
          wordHistory.add(game.getWord());
        });

    return wordHistory;
  }

  /**
   * This method takes the history of games list and grabs the words related to the specific
   * difficulty only.
   *
   * @param difficulty difficulty of word list
   * @return the list of chosen difficulty words played by user
   */
  public ArrayList<String> getWordHistory(CategorySelector.Difficulty difficulty) {
    ArrayList<String> wordHistory = new ArrayList<>();

    // iterating through each game played
    historyOfGames.forEach(
        (game) -> {

          // adding words with the same difficulty only
          if (game.getWordDifficulty() == difficulty) {
            wordHistory.add(game.getWord());
          }
        });

    return wordHistory;
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

  public boolean[] getBadges() {
    return this.badges;
  }

  public void setBadgeTrue(int badgeIndex) {
    this.badges[badgeIndex] = true;
  }

  public int getConsecutiveWins() {
    return this.consecutiveWins;
  }

  public void resetConsecutiveWins() {
    this.consecutiveWins = 0;
  }

  public void incrementConsecutiveWins() {
    this.consecutiveWins++;
  }

  public void setSoundOn(Boolean on) {
    this.soundOn = on;
  }

  public Boolean isSoundOn() {
    return this.soundOn;
  }
}
