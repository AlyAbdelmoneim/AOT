package game.gui;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.File;
public class GUI extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        String path = "src/game/gui/images/intro2.mp4";
        File introFile = new File(path);
        Media media = new Media(introFile.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        stage.show();

//        Image cursorImage = new Image("file:src/game/gui/images/cursor.png");

        Font.loadFont(
                getClass().getResource("fonts/Skranji-Bold.ttf").toExternalForm(),
                12
        );
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/introScene.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("css/HomeButtons.css").toExternalForm());
//        scene.setCursor(new ImageCursor(cursorImage, cursorImage.getWidth() / 2, cursorImage.getHeight() / 2));
        stage.setScene(scene);
        stage.setTitle("Game");
//        stage.setFullScreen(true);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
