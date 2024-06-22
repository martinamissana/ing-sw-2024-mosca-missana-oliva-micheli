package it.polimi.ingsw.view.GUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import static java.lang.System.exit;

public class TopBarController {
    private double xPos;
    private double yPos;
    private Stage stage;
    private MainLayout mainLayout;
    private final ViewSingleton viewSingleton = ViewSingleton.getInstance();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMainLayout(MainLayout mainLayout) {
        this.mainLayout = mainLayout;
    }

    @FXML
    public void exitGame() {
        if (viewSingleton.isInitialized()) viewSingleton.getView().removeObserver(mainLayout);
        Platform.exit();
        exit(0);
    }

    @FXML
    public void minimizeScreen() {
        if (stage != null) stage.setIconified(true);
    }

    @FXML
    public void setFullscreen() {
        if (stage != null) stage.setFullScreen(!stage.isFullScreen());
    }

    @FXML
    public void setPosition(MouseEvent mouseEvent) {
        xPos = stage.getX() - mouseEvent.getScreenX();
        yPos = stage.getY() - mouseEvent.getScreenY();
    }

    @FXML
    public void move(MouseEvent mouseEvent) {
        stage.setX(mouseEvent.getScreenX() + xPos);
        stage.setY(mouseEvent.getScreenY() + yPos);
    }
}
