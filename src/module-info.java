module GUI.second.Trial {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.media;
    requires junit;

    opens game.gui.views;
    opens game.gui;
}