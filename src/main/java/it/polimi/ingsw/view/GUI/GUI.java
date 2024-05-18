package it.polimi.ingsw.view.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GUI extends Application {

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage stage){

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/TitleScreen.fxml"));
            int WIDTH = 800;
            int HEIGHT = 600;
            Scene sceneTitle = new Scene(root, WIDTH, HEIGHT);

            // stage styling
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Codex Naturalis");
            stage.setResizable(false);
            stage.setScene(sceneTitle);
            stage.setOnCloseRequest(t -> {
                Platform.exit();
                System.exit(0);
            });
            stage.show();

            // assign scene to stage
            stage.setScene(sceneTitle);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}