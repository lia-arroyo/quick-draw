package nz.ac.auckland.se206.profiles;

import java.util.ArrayList;
import nz.ac.auckland.se206.difficulty.DifficultyLevel;

public class UserProfile {

  private String userName;
  private int profileIndex;
  private int wins = 0;
  private int losses = 0;
  private ArrayList<String> wordHistory = new ArrayList<>();
  private double highestPredictionPercentage = 0;
  private DifficultyLevel difficultyLevel;
  private boolean[] badges = new boolean[8];

  public UserProfile(String userName, int profileIndex) {
    this.userName = userName;
    this.profileIndex = profileIndex;
    this.difficultyLevel = new DifficultyLevel();
  }

  public String getUserName() {
    return this.userName;
  }

  public int getProfileIndex() {
    return this.profileIndex;
  }

  public int getWinsCount() {
    return this.wins;
  }

  public int getLossesCount() {
    return this.losses;
  }

  public void incrementWinsCount() {
    this.wins++;
  }

  public void incrementLossesCount() {
    this.losses++;
  }

  public ArrayList<String> getWordHistory() {
    return this.wordHistory;
  }

  /**
   * This method adds to the user's existing word history arraylist.
   *
   * @param word the word the user has played
   */
  public void addWordToHistory(String word) {
    this.wordHistory.add(word);
  }

  public double getHighestPrediction() {
    return this.highestPredictionPercentage;
  }

  public void setHighestPredictionPercentage(double percentage) {
    this.highestPredictionPercentage = percentage;
  }

  public DifficultyLevel getDifficultyLevel() {
    return this.difficultyLevel;
  }

  public boolean[] getBadges() {
    return this.badges;
  }
}
