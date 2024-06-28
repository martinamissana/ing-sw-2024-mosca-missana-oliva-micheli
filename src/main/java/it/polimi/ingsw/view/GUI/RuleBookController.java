package it.polimi.ingsw.view.GUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * Class RuleBookController
 * handles the rule book
 */
public class RuleBookController {
    @FXML
    private Button previousPage;
    @FXML
    private Button nextPage;
    @FXML
    private ImageView pageView;

    private int page = 0;
    private Pane visible;

    /**
     * Allows the "Show rules button" in Open to set the rule book
     * @param visible the pane used to visualize the rule book
     */
    public void setVisible(Pane visible) {
        this.visible = visible;
        pageView.setImage(new Image(getClass().getResource("/images/ruleBook/ruleBook0.png").toExternalForm()));
    }

    /**
     * Handles pages scrolling
     * @param mouseEvent identifies the button clicked to go to next or previous page
     */
    public void goToPage(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            if (mouseEvent.getSource().equals(previousPage)) {
                page--;
                if (page == -1) page = 6;
            } else if (mouseEvent.getSource().equals(nextPage)) {
                page++;
                if (page == 7) page = 0;
            }
            pageView.setImage(new Image(getClass().getResource("/images/ruleBook/ruleBook" + page + ".png").toExternalForm()));
        });
    }

    /**
     * Sets the pane in Open as not visible
     */
    public void closeRules() {
        visible.getChildren().clear();
        visible.setVisible(false);
    }
}
