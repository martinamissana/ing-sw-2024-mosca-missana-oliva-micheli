package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.s2c.SecretGoalAssignedMessage;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Objects;


/**
 * Class GoalsController
 * used to visualize the goals that the player should complete
 */
public class GoalsController implements ViewObserver {

    private View view;

    @FXML
    private Pane commonGoal1;
    @FXML
    private Pane commonGoal2;
    @FXML
    private Pane secretGoal;

    /**
     * sets the observable view and initializes the common goals
     * @param view the observable view
     */
    public void setView(View view) {
        this.view = view;
        view.addObserver(this);
        setCommonGoals();
    }

    /**
     * loads the images of the common goals
     */
    private void setCommonGoals(){
        commonGoal1.getChildren().add(new GoalBuilder(view.getCommonGoal1()).getGoalImage());
        commonGoal2.getChildren().add(new GoalBuilder(view.getCommonGoal2()).getGoalImage());
    }

    @Override
    public void update(NetMessage message) throws IOException {
        if (Objects.requireNonNull(message) instanceof SecretGoalAssignedMessage m) {
            Platform.runLater(() -> {
                secretGoal.getChildren().add(new GoalBuilder(m.getGoal()).getGoalImage());
            });
        }
    }
}
