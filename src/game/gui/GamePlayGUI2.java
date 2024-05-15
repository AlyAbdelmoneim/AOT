package game.gui;

import game.engine.BattlePhase;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GamePlayGUI2 extends Application {
    private Label turnNumberLabel;
    private Label scoreLabel;
    private Label currentResourcesLabel;
    private Label battlePhaseLabel;

    private Controller2 controller;
    Button passTurn;
    Button purchaseWeapon;
    Label wallHealth1;
    Label wallHealth2;
    Label wallHealth3;
    Label wallHealth4;
    Label wallHealth5;
    GridPane mainGrid;
    //    GridPane grid;
    GridPane titansGrid;

    public GamePlayGUI2() {
        turnNumberLabel = new Label();
        scoreLabel = new Label();
        currentResourcesLabel = new Label();
        battlePhaseLabel = new Label();
        passTurn = new Button("Pass Turn");
        purchaseWeapon = new Button("Purchase Weapon");
        wallHealth1 = new Label("Wall Health 1: ");
        wallHealth2 = new Label("Wall Health 2: ");
        wallHealth3 = new Label("Wall Health 3: ");
        wallHealth4 = new Label("Wall Health 4: ");
        wallHealth5 = new Label("Wall Health 5: ");
        mainGrid = new GridPane();

        titansGrid = new GridPane();
        titansGrid.setPrefWidth(1160);
        titansGrid.setPrefHeight(900);
    }

    public void setController(Controller2 controller) {
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
        hBox.getChildren().addAll(turnNumberLabel, scoreLabel, currentResourcesLabel, battlePhaseLabel, wallHealth1, wallHealth2, wallHealth3,wallHealth4, wallHealth5, passTurn, purchaseWeapon);

        labelsGrid.add(hBox, 0, 0);

        VBox walls = new VBox();
        walls.setMaxSize(40, 900);

        Pane wall1 = new Pane();
        wall1.setPrefSize(40, 180);
        Pane wall2 = new Pane();
        wall2.setPrefSize(40, 180);
        Pane wall3 = new Pane();
        wall3.setPrefSize(40, 180);
        Pane wall4 = new Pane();
        wall4.setPrefSize(40, 180);
        Pane wall5 = new Pane();
        wall5.setPrefSize(40, 180);


        wall1.getStyleClass().add("game-wall-cell");
        wall2.getStyleClass().add("game-wall-cell");
        wall3.getStyleClass().add("game-wall-cell");
        wall4.getStyleClass().add("game-wall-cell");
        wall5.getStyleClass().add("game-wall-cell");

        wall1.setOnMouseClicked(e -> handleWallButtonClick(0));
        wall2.setOnMouseClicked(e -> handleWallButtonClick(1));
        wall3.setOnMouseClicked(e -> handleWallButtonClick(2));
        wall4.setOnMouseClicked(e -> handleWallButtonClick(3));
        wall5.setOnMouseClicked(e -> handleWallButtonClick(4));



        walls.getChildren().addAll(wall1, wall2, wall3, wall4, wall5);

        HBox wallsAndTitans = new HBox();
        wallsAndTitans.getChildren().addAll(walls, titansGrid);


        // Set constraints for labelsGrid
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(10);

        mainGrid = new GridPane();

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
            wallHealth4.setText("Wall Health 4: " + wallHealth[3]);
            wallHealth5.setText("Wall Health 5: " + wallHealth[4]);
        }

    }
    public void handleWallButtonClick(int laneIndex) {
        controller.openLaneInfoWindow(laneIndex);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
