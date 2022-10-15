package nz.ac.auckland.se206.util;

import javafx.concurrent.Task;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import nz.ac.auckland.se206.profiles.UserProfileManager;

public class SoundUtils {

  private static MediaPlayer bgmPlayer;
  private static MediaPlayer gameMusicPlayer;

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

  public void playBackgroundMusic() {
    String path = this.getClass().getResource("/sounds/bgm.mp3").toString();

    /*
     * Creating a new thread to play the background music in the back, even after
     * the scenes change in the game. Also, when the music finishes, it replays
     * again and again until it is manually stopped.
     */
    Task<Void> soundTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {

            // Playing the music on loop
            Media sound = new Media(path);
            bgmPlayer = new MediaPlayer(sound);

            bgmPlayer.setVolume(0.4);

            bgmPlayer.setOnEndOfMedia(
                new Runnable() {
                  public void run() {
                    bgmPlayer.seek(Duration.ZERO);
                  }
                });
            bgmPlayer.play();

            return null;
          }
        };

    // Starting the thread
    Thread bgmThread = new Thread(soundTask);
    bgmThread.start();
  }

  public void playDrawingMusic() {
    String path = this.getClass().getResource("/sounds/canvas-music.mp3").toString();

    bgmPlayer.stop();

    Task<Void> soundTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {

            // Playing the music on loop
            Media sound = new Media(path);
            gameMusicPlayer = new MediaPlayer(sound);

            gameMusicPlayer.setVolume(0.8);

            gameMusicPlayer.setOnEndOfMedia(
                new Runnable() {
                  public void run() {
                    gameMusicPlayer.seek(Duration.ZERO);
                  }
                });
            gameMusicPlayer.play();

            return null;
          }
        };

    // Starting the thread
    Thread gameMusicThread = new Thread(soundTask);
    gameMusicThread.start();
  }

  public void stopDrawingMusic() {
    gameMusicPlayer.stop();
    bgmPlayer.play();
  }

  private void playSound(String path) {
    if (UserProfileManager.currentProfile == null) {
      Media sound = new Media(path);
      MediaPlayer mediaPlayer = new MediaPlayer(sound);
      mediaPlayer.play();
    } else {
      if (UserProfileManager.currentProfile.isSoundOn()) {
        Media sound = new Media(path);
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
      }
    }
  }
}
