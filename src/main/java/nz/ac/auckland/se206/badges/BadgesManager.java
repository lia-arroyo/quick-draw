package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.profiles.UserProfileManager;

/** This class is for managing all the badges and their related functionality. */
public class BadgesManager {

  /**
   * This method checks whether the current user can qualify for any of the 8 badges.
   *
   * @param gameTime how long the user took to finish the round
   * @param drawTime how long the user has to draw
   * @param gameConfidence the
   */
  public static void checkForBadges(int gameTime, int drawTime, double gameConfidence) {
    // Checking the game time so that the badges can be updated if needed
    if (gameTime <= 30) {
      UserProfileManager.currentProfile.setBadgeTrue(0);
    }
    if (gameTime <= 15) {
      UserProfileManager.currentProfile.setBadgeTrue(1);
    }
    if (gameTime <= 5) {
      UserProfileManager.currentProfile.setBadgeTrue(2);
    }

    // Checking if they won in the last 5 seconds to update the 4th badge
    if (gameTime >= drawTime - 5) {
      UserProfileManager.currentProfile.setBadgeTrue(3);
    }

    // Checking if the accuracy was 75% or higher to update the 5th badge
    if (gameConfidence >= 75) {
      UserProfileManager.currentProfile.setBadgeTrue(4);
    }

    // Checking for consecutive wins to update the 6th or 7th badge
    if (UserProfileManager.currentProfile.getConsecutiveWins() == 3) {
      UserProfileManager.currentProfile.setBadgeTrue(5);
    }
    if (UserProfileManager.currentProfile.getConsecutiveWins() == 10) {
      UserProfileManager.currentProfile.setBadgeTrue(6);
    }

    // Checking to update the 8th badge
    if (UserProfileManager.currentProfile.getWordHistory().size() == 200) {
      UserProfileManager.currentProfile.setBadgeTrue(7);
    }
  }
}
