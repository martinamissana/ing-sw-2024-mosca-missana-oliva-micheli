package it.polimi.ingsw.view.GUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class RuleBookController {
    @FXML
    private AnchorPane book;
    @FXML
    private Button previousPage;
    @FXML
    private Button nextPage;
    @FXML
    private ImageView pageView;

    private int page = 0;
    private Pane visible;

    public void setVisible(Pane visible) {
        this.visible = visible;
        pageView.setImage(new Image(getClass().getResource("/images/ruleBook/ruleBook0.png").toExternalForm()));
    }

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

    public void closeRules() {
        visible.getChildren().clear();
        visible.setVisible(false);
    }
}
