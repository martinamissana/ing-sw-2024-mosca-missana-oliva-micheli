package it.polimi.ingsw.view.GUIFX;

import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.view.RMIView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class GoalBuilder extends Node implements Initializable {
    @FXML
    ImageView goalImage = new ImageView();
    private Goal goal;
    private String PATH = "/images/";

    public GoalBuilder(Goal goal) {
        this.goal=goal;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String pathgenerale = RMIView.class.getResource(PATH).toString();
        pathgenerale = pathgenerale + "/goals/" + goal.getGoalID() + ".png";
        Image image = new Image(pathgenerale);
        goalImage.setImage(image);
    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }
}
