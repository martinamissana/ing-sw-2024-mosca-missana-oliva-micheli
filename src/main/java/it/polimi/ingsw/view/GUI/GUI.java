package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.view.TCPView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class GUI extends Application {
    private double xPos;
    private double yPos;

    @FXML
    private AnchorPane TopBar;
    @FXML
    private Button minimizeButton;
    @FXML
    private ToggleButton fullscreenButton;
    @FXML
    private Button quitButton;

    @Override
    public void start(Stage stage) {

        try {
            int WIDTH = 500;
            int HEIGHT = 400;

            // stage styling
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Codex Naturalis");
            stage.setMinWidth(WIDTH);
            stage.setMinHeight(HEIGHT);
            stage.setOnCloseRequest(t -> {
                Platform.exit();
                System.exit(0);
            });

            // create top bar
            Region topBar;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TopBar.fxml"));
                loader.setController(this);
                topBar = loader.load();
            }    catch (IOException e) {
                throw new RuntimeException(e);
            }

            // move stage by dragging on top bar
            TopBar.setOnMousePressed((mouseEvent) -> {
                xPos = stage.getX() - mouseEvent.getScreenX();
                yPos = stage.getY() - mouseEvent.getScreenY();
            });
            TopBar.setOnMouseDragged(mouseEvent -> {
                stage.setX(mouseEvent.getScreenX() + xPos);
                stage.setY(mouseEvent.getScreenY() + yPos);
            });

            // define top bar button controls
            minimizeButton.setOnAction(event -> stage.setIconified(true));
            fullscreenButton.setOnAction(event -> stage.setFullScreen(!stage.isFullScreen()));
            quitButton.setOnAction(event -> {
                // disconnect from tcp view before closing
                ViewSingleton viewSing = ViewSingleton.getInstance();
                if (viewSing.isInitialized()) {
                    try {
                        viewSing.getView().disconnect();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                Platform.exit();
                System.exit(0);
            });

            // create scene
            final Scene scene = new Scene(new LayoutBuilder(topBar).build(), WIDTH, HEIGHT);

            // assign scene to stage
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}