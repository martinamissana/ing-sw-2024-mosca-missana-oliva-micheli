package it.polimi.ingsw.view.GUIFX;

import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.s2c.SecretGoalAssignedMessage;
import it.polimi.ingsw.network.netMessage.s2c.SecretGoalsListAssignedMessage;
import it.polimi.ingsw.view.GUI.ViewSingleton;
import it.polimi.ingsw.view.ViewObserver;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.io.IOException;


public class GoalsController implements ViewObserver {

    private final ViewSingleton viewSing = ViewSingleton.getInstance();

    @FXML
    private Pane commonGoal1;
    @FXML
    private Pane commonGoal2;
    @FXML
    private Pane secretGoal;

    public void initialize(){
        viewSing.getView().addObserver(this);
    }

    @Override
    public void update(NetMessage message) throws IOException {
        switch (message) {
            case SecretGoalAssignedMessage m -> {
                secretGoal.getChildren().add(new GoalBuilder(m.getGoal()));
            }
            case SecretGoalsListAssignedMessage ignored -> {
                commonGoal1.getChildren().add(new GoalBuilder(viewSing.getView().getCommonGoal1()));
                commonGoal2.getChildren().add(new GoalBuilder(viewSing.getView().getCommonGoal2()));
            }
            default -> {
            }
        }
    }
}
