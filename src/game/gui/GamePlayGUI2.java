package game.gui;

import game.engine.BattlePhase;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
    Button AIButton;
    Label wallHealth1;
    Label wallHealth2;
    Label wallHealth3;
    Label wallHealth4;
    Label wallHealth5;
    GridPane mainGrid;
    GridPane titansGrid;
    Pane lane1Weapons;
    Pane lane2Weapons;
    Pane lane3Weapons;
    Pane lane4Weapons;
    Pane lane5Weapons;

    public GamePlayGUI2() {
        turnNumberLabel = new Label();
        turnNumberLabel.getStyleClass().add("GameBarlabel");
        scoreLabel = new Label();
        scoreLabel.getStyleClass().add("GameBarlabel");
        currentResourcesLabel = new Label();
        currentResourcesLabel.getStyleClass().add("GameBarlabel");
        battlePhaseLabel = new Label();
        battlePhaseLabel.getStyleClass().add("GameBarlabel");
        passTurn = new Button("Pass Turn");
        purchaseWeapon = new Button("Purchase Weapon");
        wallHealth1 = new Label();
        wallHealth2 = new Label();
        wallHealth3 = new Label();
        wallHealth4 = new Label();
        wallHealth5 = new Label();
        mainGrid = new GridPane();
        titansGrid = new GridPane();
        titansGrid.setPrefWidth(1160);
        titansGrid.setPrefHeight(900);
        lane1Weapons = new Pane();
        lane1Weapons.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        lane2Weapons = new Pane();
        lane2Weapons.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        lane3Weapons = new Pane();
        lane3Weapons.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        lane4Weapons = new Pane();
        lane4Weapons.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        lane5Weapons = new Pane();
        lane5Weapons.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        AIButton = new Button("AI");
        AIButton.getStyleClass().add("pass-turn-button");

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
        hBox.setPadding(new Insets(40, 300, 10, 10)); // Added top padding
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(20);
        hBox.getChildren().addAll(turnNumberLabel, scoreLabel, currentResourcesLabel, battlePhaseLabel,  passTurn, purchaseWeapon, AIButton);
        labelsGrid.add(hBox, 0, 0);
        passTurn.getStyleClass().add("pass-turn-button");
        purchaseWeapon.getStyleClass().add("purchase-weapon-button");


        Region labelsBackground = new Region();
        labelsBackground.setStyle("-fx-background-color: rgba(47, 47, 47, 0.8);"); // Example: semi-transparent white background

        labelsGrid.getChildren().add(labelsBackground);

        labelsBackground.prefWidthProperty().bind(labelsGrid.widthProperty());
        labelsBackground.prefHeightProperty().bind(labelsGrid.heightProperty());

        labelsBackground.toBack();

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
//        wallsAndTitans.getChildren().addAll(walls, titansGrid);

        VBox weapons = new VBox();
        weapons.setMaxSize(40, 900);
        lane1Weapons.setPrefSize(40, 300);
        lane2Weapons.setPrefSize(40, 300);
        lane3Weapons.setPrefSize(40, 300);
        lane4Weapons.setPrefSize(40, 300);
        lane5Weapons.setPrefSize(40, 300);
        weapons.getChildren().addAll(lane1Weapons, lane2Weapons, lane3Weapons, lane4Weapons, lane5Weapons);

        StackPane wallsAndWeapons = new StackPane();
        wallsAndWeapons.getChildren().addAll(walls, weapons);

        wallsAndTitans.getChildren().addAll(wallsAndWeapons, titansGrid);



        // Set constraints for labelsGrid
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(10);

        mainGrid = new GridPane();


        mainGrid.add(wallsAndTitans, 0, 1);
        mainGrid.add(labelsGrid, 0, 0);

        wallsAndTitans.setTranslateX(300);

        try {
            Image backgroundImage = new Image(new FileInputStream("src/game/gui/images/GameMap2.png"));
            BackgroundSize backgroundSize = new BackgroundSize(1600, 900, false, false, true, true);
            BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
            mainGrid.setBackground(new Background(background));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(mainGrid, 1600, 900, Color.WHITE);
        scene.getStylesheets().add(getClass().getResource("css/HomeButtons.css").toExternalForm());

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    controller.showPopupMenu(stage);
                    break;
                default:
                    break;
            }
        });
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

    public Stage getStage() {
        return (Stage) mainGrid.getScene().getWindow();
    }
}
