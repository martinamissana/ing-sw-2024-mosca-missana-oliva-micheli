package it.polimi.ingsw.view.GUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import static java.lang.System.exit;

/**
 * Class TopBarController
 * handles the action on the top bar of the stage
 */
public class TopBarController {
    private double xPos;
    private double yPos;
    private Stage stage;
    private MainLayout mainLayout;
    private final ViewSingleton viewSingleton = ViewSingleton.getInstance();

    /**
     * @param stage main stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * @param mainLayout the main layout, used to exit the game
     */
    public void setMainLayout(MainLayout mainLayout) {
        this.mainLayout = mainLayout;
    }

    /**
     * allows the player to exit the game
     */
    @FXML
    public void exitGame() {
        if (viewSingleton.isInitialized()) viewSingleton.getView().removeObserver(mainLayout);
        Platform.exit();
        exit(0);
    }

    /**
     * allows the player to minimize the window
     */
    @FXML
    public void minimizeScreen() {
        if (stage != null) stage.setIconified(true);
    }

    /**
     * allows the player to set the full screen
     */
    @FXML
    public void setFullscreen() {
        if (stage != null) stage.setFullScreen(!stage.isFullScreen());
    }

    /**
     * used to recognize the position of the window
     * @param mouseEvent mouse position
     */
    @FXML
    public void setPosition(MouseEvent mouseEvent) {
        xPos = stage.getX() - mouseEvent.getScreenX();
        yPos = stage.getY() - mouseEvent.getScreenY();
    }

    /**
     * allows the player to move the window
     * @param mouseEvent mouse position
     */
    @FXML
    public void move(MouseEvent mouseEvent) {
        stage.setX(mouseEvent.getScreenX() + xPos);
        stage.setY(mouseEvent.getScreenY() + yPos);
    }
}
