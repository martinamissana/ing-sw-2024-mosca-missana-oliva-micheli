package it.polimi.ingsw.view.GUIFX;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
            Scene sceneLogin = new Scene(root);
            stage.setTitle("CODEX NATURALIS");
            stage.setScene(sceneLogin);

            stage.setOnCloseRequest(t -> {
                Platform.exit();
                System.exit(0);
            });

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
