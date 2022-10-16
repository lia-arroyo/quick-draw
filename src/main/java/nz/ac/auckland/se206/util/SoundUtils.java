package nz.ac.auckland.se206.util;

import javafx.concurrent.Task;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import nz.ac.auckland.se206.profiles.UserProfileManager;

/** This class is for implementing the sounds in our app. */
public class SoundUtils {

  private static MediaPlayer bgmPlayer;
  private static MediaPlayer gameMusicPlayer;

  /** This method plays a 'click' sound when called to give a sound effect for buttons. */
  public void playButtonSound() {
    // Linking to the button click sound
    String path = this.getClass().getResource("/sounds/button-click.mp3").toString();
    playSound(path);
  }

  /**
   * This method plays a 'pop' sound when called to give a sound effect for revealing the first
   * letter in the game.
   */
  public void playRevealSound() {
    // Linking to the pop sound
    String path = this.getClass().getResource("/sounds/reveal-sound.mp3").toString();
    playSound(path);
  }

  /**
   * This method plays a 'click' sound when called to give a sound effect for clicking on the badges
   * in the 'My badges' page.
   */
  public void playBadgeSound() {
    // Linking to Click sound
    String path = this.getClass().getResource("/sounds/badge-sound.mp3").toString();
    playSound(path);
  }

  /**
   * This method starts the background music thread and initialises the background music player when
   * called. Once the thread starts, the music can be turned on and off by accessing the media
   * player (bgmPlayer), which is a static variable.
   */
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

            // Initialising the media player
            Media sound = new Media(path);
            bgmPlayer = new MediaPlayer(sound);

            bgmPlayer.setVolume(0.2);

            // Whenever the music ends, the music starts from 0, which means the music is on
            // a constant loop
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

  /**
   * This method starts the drawing music thread and initialises the drawing music player when
   * called. Once the thread starts, the music can be controlled by accessing gameMusicPlayer which
   * is a static variable.
   */
  public void playDrawingMusic() {
    String path = this.getClass().getResource("/sounds/canvas-music.mp3").toString();

    // Stopping the background music as we need to play the drawing music
    bgmPlayer.stop();

    /*
     * Creating a new thread to play the drawing music in the back. When the music
     * finishes, it replays again and again until it is stopped.
     */
    Task<Void> soundTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {

            // Initialising the media player
            Media sound = new Media(path);
            gameMusicPlayer = new MediaPlayer(sound);

            gameMusicPlayer.setVolume(0.3);

            // Whenever the music ends, the music starts from 0, which means the music is on
            // a constant loop.
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

  /** This method stops the drawing music when called, and plays the background music again. */
  public void stopDrawingMusic() {
    // Stopping game music so it doesn't overlap
    gameMusicPlayer.stop();
    bgmPlayer.play();
  }

  /**
   * This method plays the audio when given the path of the audio file. It only plays if the user
   * has sound effects on. If there is no current user then the sound effects are played anyway.
   *
   * @param path the path of the audio file
   */
  private void playSound(String path) {

    // Checking if there is no currently chosen profile
    if (UserProfileManager.currentProfile == null) {

      // If there isn't, then we just play the sound effects as normal
      Media sound = new Media(path);
      MediaPlayer mediaPlayer = new MediaPlayer(sound);
      mediaPlayer.play();

    } else {
      // If there is, we check if the user has enabled sound effects on
      if (UserProfileManager.currentProfile.isSoundOn()) {
        // Playing sound effects as normal
        Media sound = new Media(path);
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
      }
    }
  }
}
