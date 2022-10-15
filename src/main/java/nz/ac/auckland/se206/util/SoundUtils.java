package nz.ac.auckland.se206.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundUtils {

  public void playButtonSound() {

    String path = this.getClass().getResource("/sounds/button-click.mp3").toString();
    playSound(path);
  }

  public void playRevealSound() {
    String path = this.getClass().getResource("/sounds/reveal-sound.mp3").toString();
    playSound(path);
  }

  public void playBadgeSound() {
    String path = this.getClass().getResource("/sounds/badge-sound.mp3").toString();
    playSound(path);
  }

  private void playSound(String path) {
    Media sound = new Media(path);
    MediaPlayer mediaPlayer = new MediaPlayer(sound);
    mediaPlayer.play();
  }
}
