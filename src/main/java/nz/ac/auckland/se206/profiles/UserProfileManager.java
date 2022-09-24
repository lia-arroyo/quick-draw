package nz.ac.auckland.se206.profiles;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class UserProfileManager {

  public static ArrayList<UserProfile> USER_PROFILE_LIST;

  public static UserProfile CURRENT_PROFILE;

  public static int CURRENT_PRROFILE_INDEX;

  public static void saveToFile() {
    try {
      Gson gson = new Gson();
      Writer writer =
          Files.newBufferedWriter(
              Paths.get("src/main/resources/profiles/profiles.json"), StandardCharsets.UTF_8);
      gson.toJson(USER_PROFILE_LIST, writer);
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
