package it.polimi.ingsw.view.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class GUI
 * starts the GUI and sets the scene to login
 */
public class GUI extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainLayout.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            MainLayout controller = loader.getController();
            controller.setStage(stage);
            controller.setStyle();
            controller.setScene("Login");

            stage.setOnCloseRequest(t -> {
                Platform.exit();
                System.exit(0);
            });

            stage.show();

        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * launch the main application
     * @param args the arguments required
     */
    public static void main(String[] args) {
        launch(args);
    }
}
