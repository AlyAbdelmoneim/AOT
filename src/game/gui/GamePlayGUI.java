package game.gui;

import game.engine.BattlePhase;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GamePlayGUI extends Application {
    private Label turnNumberLabel;
    private Label scoreLabel;
    private Label currentResourcesLabel;
    private Label battlePhaseLabel;

    private Controller controller;
    Button passTurn;
    Button purchaseWeapon;
    Label wallHealth1;
    Label wallHealth2;
    Label wallHealth3;
    GridPane mainGrid;
//    GridPane grid;
    GridPane titansGrid;

    public GamePlayGUI() {
        turnNumberLabel = new Label();
        scoreLabel = new Label();
        currentResourcesLabel = new Label();
        battlePhaseLabel = new Label();
        passTurn = new Button("Pass Turn");
        purchaseWeapon = new Button("Purchase Weapon");
        wallHealth1 = new Label("Wall Health 1: ");
        wallHealth2 = new Label("Wall Health 2: ");
        wallHealth3 = new Label("Wall Health 3: ");
        mainGrid = new GridPane();
        titansGrid = new GridPane();
        titansGrid.setPrefWidth(1160);
        titansGrid.setPrefHeight(900);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Enjoy your game");

        GridPane labelsGrid = new GridPane();
        HBox hBox = new HBox();
        hBox.setPrefSize(1600, 100);
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(turnNumberLabel, scoreLabel, currentResourcesLabel, battlePhaseLabel, wallHealth1, wallHealth2, wallHealth3, passTurn, purchaseWeapon);
        labelsGrid.add(hBox, 0, 0);

        VBox walls = new VBox();
        walls.setMaxSize(40, 900);

        Pane wall1 = new Pane();
        wall1.setPrefSize(40, 300);
        Pane wall2 = new Pane();
        wall2.setPrefSize(40, 300);
        Pane wall3 = new Pane();
        wall3.setPrefSize(40, 300);

        wall1.getStyleClass().add("game-wall-cell");
        wall2.getStyleClass().add("game-wall-cell");
        wall3.getStyleClass().add("game-wall-cell");


        wall1.setOnMouseClicked(e -> handleWallButtonClick(0));
        wall2.setOnMouseClicked(e -> handleWallButtonClick(1));
        wall3.setOnMouseClicked(e -> handleWallButtonClick(2));


        walls.getChildren().addAll(wall1, wall2, wall3);

        HBox wallsAndTitans = new HBox();
        wallsAndTitans.getChildren().addAll(walls, titansGrid);


        // Set constraints for labelsGrid
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(10);

        ImageView imageView = new ImageView(new Image(new FileInputStream("src/game/gui/images/GameTown.png")));
        imageView.setFitHeight(900);
        imageView.setFitWidth(400);
        imageView.setPreserveRatio(true);
        imageView.setTranslateX(0);
        imageView.setTranslateY(0);

        mainGrid = new GridPane();

//        mainGrid.add(imageView, 0, 0);

//        mainGrid.setBackground(new Background(new BackgroundImage(new Image(new FileInputStream("src/game/gui/images/GAME BACKGROUND updated2.jpg")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));


//        mainGrid.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));

        mainGrid.add(labelsGrid, 0, 0);

        mainGrid.add(wallsAndTitans, 0, 1);

        wallsAndTitans.setTranslateX(300);

        Scene scene = new Scene(mainGrid, 1600, 900, Color.WHITE);
        scene.getStylesheets().add(getClass().getResource("css/HomeButtons.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void updateLabels(int turnNumber, int score, int currentResources, BattlePhase battlePhase, int[] wallHealth) {
        this.turnNumberLabel.setText("Turn number : " + turnNumber);
        this.scoreLabel.setText("Score : " + score);
        this.currentResourcesLabel.setText("Current Resources : " + currentResources);
        this.battlePhaseLabel.setText("Battle Phase : " + battlePhase);

        if (wallHealth.length >= 3) {
            wallHealth1.setText("Wall Health 1: " + wallHealth[0]);
            wallHealth2.setText("Wall Health 2: " + wallHealth[1]);
            wallHealth3.setText("Wall Health 3: " + wallHealth[2]);
        }
    }
    public Stage getStage() {
        return (Stage) mainGrid.getScene().getWindow();
    }
    public void handleWallButtonClick(int laneIndex) {
        controller.openLaneInfoWindow(laneIndex);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
