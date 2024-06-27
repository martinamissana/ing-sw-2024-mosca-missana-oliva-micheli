package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.goal.Goal;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Class GoalBuilder
 * used to generate image of the goal
 */
public class GoalBuilder {
    @FXML
    ImageView goalImage = new ImageView();

    private Goal goal;
    private String PATH = "/images";

    /**
     * Class constructor
     * @param goal the goal
     */
    public GoalBuilder(Goal goal) {
        this.goal=goal;
        initialize();
    }

    /**
     * used to find the path of the image and load it
     */
    public void initialize() {
        String generalPath = getClass().getResource(PATH).toExternalForm();
        generalPath = generalPath + "/goals/" + goal.getGoalID() + ".png";
        Image image = new Image(generalPath);
        goalImage.setFitWidth(100);
        goalImage.setFitHeight(66.66);
        goalImage.setImage(image);
    }

    /**
     * @return ImageView
     */
    public ImageView getGoalImage() {
        return goalImage;
    }
}
