package nz.ac.auckland.se206;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nz.ac.auckland.se206.profiles.UserProfile;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {

  public static String CURRENT_USER = "";

  public static List<UserProfile> userProfileList;

  public static void main(final String[] args) {
    launch();
  }

  /**
   * Returns the node associated to the input file. The method expects that the file is located in
   * "src/main/resources/fxml".
   *
   * @param fxml The name of the FXML file (without extension).
   * @return The node of the input file.
   * @throws IOException If the file is not found.
   */
  public static Parent loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
   */
  @Override
  public void start(final Stage stage) throws IOException {

    loadProfilesFromJson();

    final Scene scene = new Scene(loadFxml("title"), 840, 680);

    stage.setScene(scene);
    stage.show();

    // Makes sure all threads are closed when the window closes
    stage.setOnCloseRequest(
        e -> {
          Platform.exit();
          System.exit(0);
        });
  }

  private static void loadProfilesFromJson() throws IOException {
    // Reading the JSON file
    Gson gson = new Gson();
    FileReader fr = new FileReader("./src/main/resources/profiles/profiles.json");
    BufferedReader br = new BufferedReader(fr);
    String jsonString = br.readLine();

    // Initialising the userProfileList
    if (jsonString != null) {
      UserProfile[] users = gson.fromJson(jsonString, UserProfile[].class);
      userProfileList = Arrays.asList(users);
    } else {
      userProfileList = new ArrayList<>();
    }
  }
}
