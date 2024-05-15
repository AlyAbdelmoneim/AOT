package game.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class IntroScene {
    MediaPlayer mediaPlayer;

    @FXML
    private MediaView mediaView;
    private AudioClip clickSoundClip;

    public IntroScene() {
        this.clickSoundClip = new AudioClip(getClass().getResource("sounds/click-effect-updated.wav").toString());
    }

    public void clickSoundEffect() {
        clickSoundClip.play();
    }

    public void initialize() {
        if (mediaView != null) {
            playVideo();
        }
    }

    public void playVideo() {
        String path = "src/game/gui/images/intro2.mp4";
        Media media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        Screen screen = Screen.getPrimary();
        mediaView.setFitHeight(screen.getVisualBounds().getHeight() + 10);
        mediaView.setFitWidth(screen.getVisualBounds().getWidth() + 300);
        mediaPlayer.setOnEndOfMedia(() -> {
            try {
                switchToNextScene();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        mediaPlayer.setAutoPlay(true);
    }

    @FXML
    private void switchToNextScene() throws IOException {
        mediaPlayer.stop();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/gui.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) mediaView.getScene().getWindow();
        stage.setScene(scene);
//        stage.setFullScreen(true);
        stage.show();
    }
}
