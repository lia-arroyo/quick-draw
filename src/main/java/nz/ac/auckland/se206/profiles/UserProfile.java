package nz.ac.auckland.se206.profiles;

public class UserProfile {

  private String userName;
  private int profileID;
  private int wins = 0;
  private int losses = 0;

  public UserProfile(String userName, int profileID) {
    this.userName = userName;
    this.profileID = profileID;
  }

  public String getUserName() {
    return this.userName;
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
}
