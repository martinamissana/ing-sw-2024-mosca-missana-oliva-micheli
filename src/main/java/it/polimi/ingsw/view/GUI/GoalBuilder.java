package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.goal.Goal;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GoalBuilder {
    @FXML
    ImageView goalImage = new ImageView();

    private Goal goal;
    private String PATH = "/images";

    public GoalBuilder(Goal goal) {
        this.goal=goal;
        initialize();
    }

    public void initialize() {
        String pathgenerale = getClass().getResource(PATH).toExternalForm();
        pathgenerale = pathgenerale + "/goals/" + goal.getGoalID() + ".png";
        Image image = new Image(pathgenerale);
        goalImage.setFitWidth(100);
        goalImage.setFitHeight(66.66);
        goalImage.setImage(image);
    }

    public ImageView getGoalImage() {
        return goalImage;
    }
}
