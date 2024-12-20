package game.gui;

import game.engine.Battle;
import game.engine.exceptions.InsufficientResourcesException;
import game.engine.exceptions.InvalidLaneException;
import game.engine.lanes.Lane;
import game.engine.titans.AbnormalTitan;
import game.engine.titans.ArmoredTitan;
import game.engine.titans.ColossalTitan;
import game.engine.titans.Titan;
import game.engine.weapons.*;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
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

    private boolean gameOverTriggered = false;
    private HashSet<Integer>[] weaponsInLanes;
    public Controller() throws Exception {
        easyBattle = new Battle(1, 0, 80, 3, 250);
        gamePlayGUI = new GamePlayGUI();
        gamePlayGUI.setController(this);
        gamePlayGUI.start(new Stage());
        gamePlayGUI.passTurn.setOnAction(e -> passTurn());
        gamePlayGUI.purchaseWeapon.setOnAction(e -> chooseWeaponAndLane(new Stage()));
        gamePlayGUI.lane1Weapons.setOnMouseClicked(e->openLaneInfoWindow(0));
        gamePlayGUI.lane2Weapons.setOnMouseClicked(e->openLaneInfoWindow(1));
        gamePlayGUI.lane3Weapons.setOnMouseClicked(e->openLaneInfoWindow(2));
        gamePlayGUI.AIButton.setOnAction(e -> {
            try {
                AImove(3);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        lanes = new ArrayList<>();
        lanes.addAll(easyBattle.getLanes());
        weaponsInLanes = new HashSet[3];
        for(int i = 0; i < 3; i++){
            weaponsInLanes[i] = new HashSet<>();
        }
        updateUI();
    }

    private void updateUI() {
        updateLabels();
        addTitans();
        updateWeapons();
    }

    private void updateLabels() {
        int[] wallHealths = new int[3];
        for (int i = 0; i < 3; i++) {
            wallHealths[i] = easyBattle.getOriginalLanes().get(i).getLaneWall().getCurrentHealth();
        }
        gamePlayGUI.updateLabels(easyBattle.getNumberOfTurns(), easyBattle.getScore(),
                easyBattle.getResourcesGathered(), easyBattle.getBattlePhase(), wallHealths);
    }
    private void updateWeapons(){
        for(int i = 0; i < 3; i++) {
            for(int x : weaponsInLanes[i]){
                Circle circle = new Circle(40);
                int offset = -30;
                if(x == 1) {
                    circle.setFill(new ImagePattern(new Image(new File("src/game/gui/images/piercing.png").toURI().toString())));
                    offset += 20;
                }
                else if(x == 2) {
                    circle.setFill(new ImagePattern(new Image(new File("src/game/gui/images/sniper.png").toURI().toString())));
                    offset += 70;
                }
                else if(x == 3) {
                    circle.setFill(new ImagePattern(new Image(new File("src/game/gui/images/volley.png").toURI().toString())));
                    offset += 110;
                }
                else {
                    circle.setFill(new ImagePattern(new Image(new File("src/game/gui/images/walltrap.png").toURI().toString())));
                    offset += 150;
                }
                gamePlayGUI.lane1Weapons.getChildren().add(circle);
                circle.setTranslateX(0);
                circle.setTranslateY(i * 300 + offset);
            }
        }
    }
    public void passTurn() {
        try {
            easyBattle.passTurn();
        } catch (NullPointerException e) {
            if (!gameOverTriggered) {
                gameOverTriggered = true;
                playGameOverSound();
                gameEndStage("Game Over");
            }
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

        // Set modality and owner to ensure the popup is on top
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(gamePlayGUI.getStage());

        stage.show();
    }


    public void playGameOverSound() {
        String musicFile = "src/game/gui/sounds/gameover.mp3"; // Path to your game over MP3 file
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
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
        if(weaponIndex == 0){
            if(weaponsInLanes[laneIndex] == null){
                HashSet<Integer> tmp = new HashSet<>();
                tmp.add(1);
                weaponsInLanes[laneIndex] = tmp;
            }
            else {
                weaponsInLanes[laneIndex].add(1);
            }
        }
        else if(weaponIndex == 1){
            if(weaponsInLanes[laneIndex] == null){
                HashSet<Integer> tmp = new HashSet<>();
                tmp.add(2);
                weaponsInLanes[laneIndex] = tmp;
            }
            else {
                weaponsInLanes[laneIndex].add(2);
            }

        }
        else if(weaponIndex == 2){
            if(weaponsInLanes[laneIndex] == null){
                HashSet<Integer> tmp = new HashSet<>();
                tmp.add(3);
                weaponsInLanes[laneIndex] = tmp;
            }
            else {
                weaponsInLanes[laneIndex].add(3);
            }
        }
        else{
            if(weaponsInLanes[laneIndex] == null){
                HashSet<Integer> tmp = new HashSet<>();
                tmp.add(4);
                weaponsInLanes[laneIndex] = tmp;
            }
            else {
                weaponsInLanes[laneIndex].add(4);
            }

        }
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
                    circle.setFill(new ImagePattern(new Image(new File("src/game/gui/images/ColossalPng.png").toURI().toString())));
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

    public void showPopupMenu(Stage primaryStage) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(primaryStage);

        VBox popupVBox = new VBox();
        popupVBox.setSpacing(10);
        popupVBox.setPadding(new Insets(20));

        Label sureLabel = new Label("Are you sure you want to exit the game?");

        Button continueButton = new Button("Continue");
        continueButton.setOnAction(e -> popupStage.close());

        Button homeButton = new Button("Return To Home");
        homeButton.setOnAction(e -> {
            // Code to return to home screen
            try {
                returnToHomeScreen();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            // Assuming you have a method to launch the home screen
            // new HomeScreen().start(new Stage());
        });

        // Add CSS classes
        popupVBox.getStyleClass().add("popup-vbox");
        sureLabel.getStyleClass().add("exitLabel");
        continueButton.getStyleClass().add("inGamePopupContinue");
        homeButton.getStyleClass().add("inGamePopupExit");

        popupVBox.getChildren().addAll(sureLabel, continueButton, homeButton);

        Scene popupScene = new Scene(popupVBox, 700, 300);
        popupScene.getStylesheets().add(getClass().getResource("css/HomeButtons.css").toExternalForm());
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
    public void purchaseAI(int weaponNo, int laneNo){
        if(weaponNo-1 == 0){
            if(weaponsInLanes[laneNo] == null){
                HashSet<Integer> tmp = new HashSet<>();
                tmp.add(1);
                weaponsInLanes[laneNo] = tmp;
            }
            else {
                weaponsInLanes[laneNo].add(1);
            }
        }
        else if(weaponNo-1 == 1){
            if(weaponsInLanes[laneNo] == null){
                HashSet<Integer> tmp = new HashSet<>();
                tmp.add(2);
                weaponsInLanes[laneNo] = tmp;
            }
            else {
                weaponsInLanes[laneNo].add(2);
            }

        }
        else if(weaponNo-1 == 2){
            if(weaponsInLanes[laneNo] == null){
                HashSet<Integer> tmp = new HashSet<>();
                tmp.add(3);
                weaponsInLanes[laneNo] = tmp;
            }
            else {
                weaponsInLanes[laneNo].add(3);
            }
        }
        else{
            if(weaponsInLanes[laneNo] == null){
                HashSet<Integer> tmp = new HashSet<>();
                tmp.add(4);
                weaponsInLanes[laneNo] = tmp;
            }
            else {
                weaponsInLanes[laneNo].add(4);
            }

        }
        try {
            easyBattle.purchaseWeapon(weaponNo, easyBattle.getOriginalLanes().get(laneNo));
        } catch (InsufficientResourcesException e) {
            exceptionStage(e.getMessage());
        } catch (InvalidLaneException e) {
            exceptionStage(e.getMessage());
        } catch (NullPointerException npe){
            gameOverTriggered = true;
            playGameOverSound();
            gameEndStage("Game Over");
        }
        updateUI();

    }
    int bestScore;
    String moves;
    int bestDanger;
    public void AImove(int turnsAhead) throws IOException {
        bestScore = -1;
        moves = "";
        backtracking(turnsAhead, "", easyBattle.copy());
        if(moves.charAt(0) == '0'){
            try{
                easyBattle.passTurn();
            } catch (NullPointerException e) {
                if (!gameOverTriggered) {
                    gameOverTriggered = true;
                    playGameOverSound();
                    gameEndStage("Game Over");
                }

            }
        }
        else purchaseAI(moves.charAt(0) - '0', moves.charAt(1) - '0');
        updateUI();

    }
    public void backtracking(int turnAhead, String moves, Battle b) throws IOException {
        if(turnAhead == 0){
            if(bestScore == -1 || b.getScore() > bestScore){
                bestScore = b.getScore();
                this.moves = moves;
                bestDanger = b.getDangerSum();
            }
            else if(b.getScore() == bestScore && b.getDangerSum() < bestDanger){
                bestScore = b.getScore();
                this.moves = moves;
                bestDanger = b.getDangerSum();
            }
            return;
        }
        for(int i = 0; i < 3; i++){
            for(int j = 1; j <= 4; j++){
                if(b.getResourcesGathered() < b.getWeaponFactory().getWeaponShop().get(j).getPrice()) continue;
                if(b.getOriginalLanes().get(i).isLaneLost()) continue;
                Battle b2 = b.copy();
                try {
                    b2.purchaseWeapon(j, b2.getOriginalLanes().get(i));
                } catch (InsufficientResourcesException e) {
                    continue;
                } catch (InvalidLaneException e) {
                    continue;
                }
//                b2.passTurn();
                backtracking(turnAhead - 1, moves + j + i, b2);
            }
        }
        backtracking(turnAhead - 1, moves + 0+0, b.copy());

    }


}