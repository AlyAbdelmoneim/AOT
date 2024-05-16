package game.gui;

import game.engine.Battle;
import game.engine.exceptions.InsufficientResourcesException;
import game.engine.exceptions.InvalidLaneException;
import game.engine.lanes.Lane;
import game.engine.titans.AbnormalTitan;
import game.engine.titans.ArmoredTitan;
import game.engine.titans.ColossalTitan;
import game.engine.titans.Titan;
import game.engine.weapons.Weapon;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static javafx.application.Application.launch;

public class Controller {
    private Battle easyBattle;
    private GamePlayGUI gamePlayGUI;

    private ArrayList<Lane> lanes = new ArrayList<>();

    public Controller() throws Exception {
        easyBattle = new Battle(1, 0, 80, 3, 250);
        gamePlayGUI = new GamePlayGUI();
        gamePlayGUI.setController(this);
        updateUI();
        gamePlayGUI.start(new Stage());
        gamePlayGUI.passTurn.setOnAction(e -> passTurn());
        gamePlayGUI.purchaseWeapon.setOnAction(e -> chooseWeaponAndLane(new Stage()));
        lanes = new ArrayList<>();
        lanes.addAll(easyBattle.getLanes());
    }

    private void updateUI() {
        updateLabels();
        addTitans();
    }

    private void updateLabels() {
        int[] wallHealths = new int[3];
        for (int i = 0; i < 3; i++) {
            wallHealths[i] = easyBattle.getOriginalLanes().get(i).getLaneWall().getCurrentHealth();
        }
        gamePlayGUI.updateLabels(easyBattle.getNumberOfTurns(), easyBattle.getScore(),
                easyBattle.getResourcesGathered(), easyBattle.getBattlePhase(), wallHealths);
    }

    public void passTurn() {
        try {
            easyBattle.passTurn();
        } catch (NullPointerException e) {
            gameEndStage("Game Over");
        }
        updateUI();
        gamePlayGUI.updateLabels(easyBattle.getNumberOfTurns(), easyBattle.getScore(),
                easyBattle.getResourcesGathered(), easyBattle.getBattlePhase(),
                new int[]{easyBattle.getOriginalLanes().get(0).getLaneWall().getCurrentHealth(),
                        easyBattle.getOriginalLanes().get(1).getLaneWall().getCurrentHealth(),
                        easyBattle.getOriginalLanes().get(2).getLaneWall().getCurrentHealth()});
    }

    public void exceptionStage(String message) {
        Pane pane = new Pane();
        Label label = new Label(message);
        pane.getChildren().add(label);
        Scene scene = new Scene(pane, 300, 200);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
    public void gameEndStage(String message) {
        Pane pane = new Pane();
        Label label = new Label(message);
        Label score = new Label("Score: " + easyBattle.getScore());
        score.setLayoutX(10);
        score.setLayoutY(30);
        Button returnToHomeScreen = new Button("Return to Home Screen");
        returnToHomeScreen.setOnAction(e -> {
            try {
                returnToHomeScreen();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        pane.getChildren().addAll(label, score, returnToHomeScreen);
        Scene scene = new Scene(pane, 300, 200);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

    }

    public void returnToHomeScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/gui.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        gamePlayGUI.getStage().close();
    }


    public void purchaseWeapon(int laneIndex, int weaponIndex) {
        Lane lane = easyBattle.getOriginalLanes().get(laneIndex);
        try {
            easyBattle.purchaseWeapon(weaponIndex + 1, lane);
        } catch (InsufficientResourcesException e) {
            exceptionStage(e.getMessage());
        } catch (InvalidLaneException e) {
            exceptionStage(e.getMessage());
        }
        updateUI();
    }

    public void chooseWeaponAndLane(Stage stage) {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);

        Label weaponLabel = new Label("Choose Weapon");
        ComboBox<String> weaponComboBox = new ComboBox<>(FXCollections.observableArrayList(
                "Piercing Cannon \n Type : Anti Titan Shell   \n Price : 25  \n Damage : 10"
                , "Sniper Cannon      \n Type: Long Range Spear    \n Price : 25  \n Damage : 35"
                , "Volley Spread      \n Type : Wall Spread Cannon \n Price : 100 \n Damage : 5"
                , "Wall Trap          \n Type : Proximity Trap     \n Price : 75  \n Damage : 100"
        )
        );
        weaponComboBox.getSelectionModel().selectFirst();

        Label laneLabel = new Label("Choose Lane");
        ComboBox<String> laneComboBox = new ComboBox<>(FXCollections.observableArrayList("Lane 1", "Lane 2", "Lane 3"));
        laneComboBox.getSelectionModel().selectFirst();

        Button purchaseButton = new Button("Purchase");
        purchaseButton.setOnAction(e -> {
            int weaponIndex = weaponComboBox.getSelectionModel().getSelectedIndex();
            int laneIndex = laneComboBox.getSelectionModel().getSelectedIndex();
            purchaseWeapon(laneIndex, weaponIndex);
            stage.close();
        });

        vBox.getChildren().addAll(weaponLabel, weaponComboBox, laneLabel, laneComboBox, purchaseButton);
        Scene scene = new Scene(vBox, 300, 200);
        stage.setScene(scene);
        stage.show();
    }
    public void openLaneInfoWindow(int laneIndex) {
        Pane laneInfoPane = new Pane();
        laneInfoPane.setPrefSize(300, 200);

//        Label laneInfoLabel = new Label("Lane Information");
        Label laneDangerLevel = new Label("Lane Danger Level: " + easyBattle.getOriginalLanes().get(laneIndex).getDangerLevel());
        Label wallCurrentHealth = new Label("Wall Current Health: " + easyBattle.getOriginalLanes().get(laneIndex).getLaneWall().getCurrentHealth());
        Label AvailableWeapons = new Label("Available Weapons: " + getWeaponsOfLane(easyBattle.getOriginalLanes().get(laneIndex)));
        Label numberOfAvailableTitans = new Label("Available Titans: " + getTitansOfLane(easyBattle.getOriginalLanes().get(laneIndex)));
        Label isLaneLost;
        if(easyBattle.getOriginalLanes().get(laneIndex).isLaneLost()) {
            isLaneLost = new Label("Lane is Lost");
            isLaneLost.setTextFill(Color.RED);
        }
        else {
            isLaneLost = new Label("Lane is Available");
            isLaneLost.setTextFill(Color.GREEN);
        }

        Button exit = new Button("Exit");

//        laneInfoLabel.setLayoutX(10);
//        laneInfoLabel.setLayoutY(10);

        wallCurrentHealth.setLayoutX(10);
        wallCurrentHealth.setLayoutY(40);

        laneDangerLevel.setLayoutX(10);
        laneDangerLevel.setLayoutY(70);

        AvailableWeapons.setLayoutX(10);
        AvailableWeapons.setLayoutY(100);

        numberOfAvailableTitans.setLayoutX(10);
        numberOfAvailableTitans.setLayoutY(130);

        isLaneLost.setLayoutX(10);
        isLaneLost.setLayoutY(160);

        exit.setLayoutX(10);
        exit.setLayoutY(190);

        exit.setOnAction(e -> ((Stage) exit.getScene().getWindow()).close());

        laneInfoPane.getChildren().addAll(laneDangerLevel, wallCurrentHealth, AvailableWeapons, numberOfAvailableTitans, exit);

        Scene scene = new Scene(laneInfoPane, 300, 200);
        Stage stage = new Stage();
        stage.setScene(scene);
        laneIndex++;
        stage.setTitle("Lane " + laneIndex + " Information");
        stage.show();
    }
    private String getWeaponsOfLane(Lane lane) {
        String weapons = "( ";
        HashSet<String> weaponsSet = new HashSet<>();
        for (Weapon weapon : lane.getWeapons()) {
            String name = "";
            switch (weapon.getClass().getSimpleName()) {
                case "PiercingCannon":
                    name = "Piercing Cannon";
                    break;
                case "SniperCannon":
                    name = "Sniper";
                    break;
                case "VolleySpreadCannon":
                    name = "Volley Spread";
                    break;
                case "WallTrap":
                    name = "Wall Trap";
                    break;
            }
            weaponsSet.add(name);
        }
        for (String weapon : weaponsSet) {
            weapons += weapon + ", ";
        }
        weapons += ")";
        return weapons;
    }
    private String getTitansOfLane(Lane lane) {
        StringBuilder titans = new StringBuilder("( ");
        HashSet<String> titansSet = new HashSet<>();

        for (Titan titan : lane.getTitans()) {
            String name = titan.getClass().getSimpleName();
            titansSet.add(name);
        }

        for (String titan : titansSet) {
            titans.append(titan).append(", ");
        }

        titans.append(")");
        return titans.toString();
    }


    public static void openTitanInfoWindow(String titanName, int titanHealth, int titanDamage, int distance, int height) {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);

        Label label = new Label("Titan Information");
        Label titanNameLabel = new Label("Titan Name: " + titanName);
        Label titanHealthLabel = new Label("Titan Health: " + titanHealth);
        Label titanDamageLabel = new Label("Titan Damage: " + titanDamage);
        Label distanceLabel = new Label("Distance: " + distance);
        Label heightLabel = new Label("Height: " + height);

        vBox.getChildren().addAll(label, titanNameLabel, titanHealthLabel, titanDamageLabel, distanceLabel,heightLabel);

        Pane pane = new Pane(vBox);

        Scene scene = new Scene(pane, 300, 200);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void addTitans() {
        GridPane titans = gamePlayGUI.titansGrid;
        titans.getChildren().clear();
        int counter = 0;
        for (Lane l : lanes) {
            for (Titan t : l.getTitans()) {

                // add check here for the titan type
                Circle circle = new Circle(30);
                String titanName = t.getClass().getSimpleName();
                int offset = 0;
                if(t instanceof ColossalTitan) {
                    circle.setFill(new ImagePattern(new Image(new File("src/game/gui/images/ColossalPng.jpeg").toURI().toString())));
                    offset = 50;
                }
                else if(t instanceof ArmoredTitan) {
                    circle.setFill(new ImagePattern(new Image(new File("src/game/gui/images/armored.png").toURI().toString())));
                    offset = 100;

                }
                else if(t instanceof AbnormalTitan) {
                    circle.setFill(new ImagePattern(new Image(new File("src/game/gui/images/abnormal.png").toURI().toString())));
                    offset = 150;
                }
                else {
                    circle.setFill(new ImagePattern(new Image(new File("src/game/gui/images/pure.png").toURI().toString())));
                    offset = 200;
                }

                circle.setOnMouseClicked(e -> openTitanInfoWindow(titanName, t.getCurrentHealth(), t.getDamage(), t.getDistance(), t.getHeightInMeters()));
                titans.getChildren().add(circle);


                if (!t.hasReachedTarget()) {
                    TranslateTransition transition = new TranslateTransition(Duration.seconds(1), circle);
                    transition.setFromX(t.getDistance() * 10 + t.getSpeed() * 10);
                    transition.setFromY(counter * 300 + offset);
                    transition.setToX(t.getDistance() * 10);
                    transition.setToY(counter * 300 + offset);
                    transition.play();
                } else {
                    circle.setTranslateX(t.getDistance() * 10);
                    circle.setTranslateY(counter * 300 + offset);
                }
            }
            counter++;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
