package game.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {
    private AudioClip clickSoundClip;
    AudioClip backgroundMusicClip;
    private boolean isMusicPlaying = false;
    private ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("images/play.png")));
    public HomeController() {
        this.clickSoundClip = new AudioClip(getClass().getResource("sounds/click-effect-updated.wav").toString());
        this.backgroundMusicClip = new AudioClip(getClass().getResource("sounds/backGroundMusic.mp3").toString());
    }
    private void switchScene(String fxmlFile, ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void backgroundMusicControl() {
        if (isMusicPlaying) {
            backgroundMusicClip.stop();
        } else {
            backgroundMusicClip.play();
        }
        isMusicPlaying = !isMusicPlaying;
    }

    public void clickSoundEffect() {
        clickSoundClip.play();
    }

    private void switchToExitScene(String fxmlFile) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void switchToRulesScene(ActionEvent event) throws IOException {
        switchScene("views/rules.fxml", event);
    }
    @FXML
    public void switchToHomeScene(ActionEvent event) throws IOException {
        switchScene("views/gui.fxml", event);
    }
    @FXML
    public void exitGame() throws IOException {
        switchToExitScene("views/exitWindow.fxml");
    }
    @FXML
    public void switchToChoiceScene(ActionEvent event) throws IOException {
        switchScene("views/ChoiceScene.fxml", event);
        //close the media playing in the background
//        backgroundMusicClip.stop();
    }
    @FXML
    private void switchToGamePlaySceneEasy(ActionEvent event) throws Exception {
        Controller controller = new Controller();

        // Get the current stage and close it
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        backgroundMusicClip.stop();
    }
    @FXML
    private void switchToGamePlaySceneMedium(ActionEvent event) throws Exception {
        Controller2 controller = new Controller2();

        // Get the current stage and close it
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        backgroundMusicClip.stop();
    }

}
