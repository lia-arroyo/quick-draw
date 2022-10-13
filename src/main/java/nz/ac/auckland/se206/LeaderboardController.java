package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class LeaderboardController {

  @FXML
  private void onReturn(ActionEvent event) {
    // Getting the scene information
    Button button = (Button) event.getSource();
    Scene currentScene = button.getScene();

    // Change to main menu page
    try {
      currentScene.setRoot(App.loadFxml("main_menu"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
