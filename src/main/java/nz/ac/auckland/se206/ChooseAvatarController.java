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
    String profileIndex = "";

    for (int i = 0; i < avatarId.length(); i++) {
      // If the character is a number
      if (avatarId.charAt(i) >= 48 && avatarId.charAt(i) <= 57) {
        profileIndex = profileIndex + avatarId.charAt(i);
      }
    }

    UserProfileManager.chosenProfileIndex = Integer.valueOf(profileIndex);

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
