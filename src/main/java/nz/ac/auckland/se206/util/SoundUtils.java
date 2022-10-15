package nz.ac.auckland.se206.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundUtils {

  public void playButtonSound() {

    String path = this.getClass().getResource("/sounds/button-click.mp3").toString();

    Media sound = new Media(path);
    MediaPlayer mediaPlayer = new MediaPlayer(sound);
    mediaPlayer.play();
  }
}
