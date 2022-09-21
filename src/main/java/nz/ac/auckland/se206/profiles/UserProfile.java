package nz.ac.auckland.se206.profiles;

public class UserProfile {

  private String username;

  public UserProfile(String username) {
    this.username = username;
  }

  private String getUsername() {
    return this.username;
  }
}
