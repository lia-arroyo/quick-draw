package nz.ac.auckland.se206.profiles;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class UserProfileManager {

  public static ArrayList<UserProfile> userProfileList;

  public static String chosenUsername = "";

  public static int chosenProfileIndex = 1;

  public static int currentProfileIndex = -1;

  // this is a reference to the current profile.
  public static UserProfile currentProfile;

  public static void saveToFile() {
    try {
      Gson gson = new Gson();
      Writer writer =
          Files.newBufferedWriter(
              Paths.get("src/main/resources/profiles/profiles.json"), StandardCharsets.UTF_8);
      gson.toJson(userProfileList, writer);
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
