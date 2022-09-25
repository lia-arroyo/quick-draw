package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.profiles.UserProfileManager;

public class ChooseAvatarController {

  @FXML private VBox avatarBox;

  public void initialize() {
    int imageIndex = 1;

    for (Node hbox : avatarBox.getChildren()) {
      HBox avatarRow = (HBox) hbox;

      for (Node avatarButton : avatarRow.getChildren()) {

        Button avatar = (Button) avatarButton;

        ImageView avatarImage =
            new ImageView(
                new Image(
                    this.getClass()
                        .getResource(String.format("/images/profileImages/%d.PNG", imageIndex))
                        .toString()));

        avatarImage.setFitWidth(avatar.getPrefWidth());
        avatarImage.setFitHeight(avatar.getPrefHeight());

        avatar.setGraphic(avatarImage);
        avatar.setId(String.format("avatar_%d", imageIndex));

        imageIndex++;
      }
    }
  }

  @FXML
  private void onGoBack(ActionEvent event) {
    goToCreateProfile(event);
  }

  @FXML
  private void onClickImage(ActionEvent event) {
    Button avatarButton = (Button) event.getSource();
    String avatarId = avatarButton.getId();

    UserProfileManager.chosenProfileIndex = Character.getNumericValue(avatarId.charAt(7));
    System.out.println(avatarId);
    goToCreateProfile(event);
  }

  private void goToCreateProfile(ActionEvent event) {
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();

    try {
      sceneButtonIsIn.setRoot(App.loadFxml("create_profile"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
