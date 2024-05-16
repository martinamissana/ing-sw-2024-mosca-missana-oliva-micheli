package it.polimi.ingsw.view.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class TitleScreenController {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    @FXML
    private Button startBtn;

    public void goToLoginScreen(ActionEvent actionEvent) {

        // go to login screen
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/LoginScreen.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, WIDTH, HEIGHT);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
