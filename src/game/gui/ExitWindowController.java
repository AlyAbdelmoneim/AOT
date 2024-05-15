package game.gui;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class ExitWindowController {

    // Method called when user confirms exit
    public void exitConfirmed(ActionEvent event) {
        System.exit(0);
    }

    // Method called when user cancels exit
    public void exitCancelled(ActionEvent event) {
        // Get the stage from the event
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();

        // Close the exit confirmation window
        stage.close();
    }
}
