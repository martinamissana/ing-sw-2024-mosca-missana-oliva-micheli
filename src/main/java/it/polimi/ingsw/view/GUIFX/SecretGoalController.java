package it.polimi.ingsw.view.GUIFX;

import it.polimi.ingsw.controller.exceptions.IllegalGoalChosenException;
import it.polimi.ingsw.controller.exceptions.UnexistentUserException;
import it.polimi.ingsw.controller.exceptions.WrongGamePhaseException;
import it.polimi.ingsw.model.exceptions.GameDoesNotExistException;
import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.view.GUI.ViewSingleton;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class SecretGoalController {
    private final ViewSingleton viewSing = ViewSingleton.getInstance();
    private final Goal first = viewSing.getView().getSecretGoalChoices().getFirst();
    private final Goal second = viewSing.getView().getSecretGoalChoices().get(1);

    @FXML
    Pane firstGoal;
    @FXML
    Pane secondGoal;

    @FXML
    public void chooseGoal(MouseEvent mouseEvent){
        if(mouseEvent.getSource().equals(firstGoal)){
            try {
                viewSing.getView().chooseSecretGoal(first.getGoalID());
            } catch (IOException | IllegalGoalChosenException | WrongGamePhaseException | GameDoesNotExistException |
                     UnexistentUserException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if(mouseEvent.getSource().equals(secondGoal)){
            try {
                viewSing.getView().chooseSecretGoal(second.getGoalID());
            } catch (IOException | IllegalGoalChosenException | WrongGamePhaseException | GameDoesNotExistException |
                     UnexistentUserException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void initialize(){
        firstGoal.getChildren().add(new GoalBuilder(first).getGoalImage());
        secondGoal.getChildren().add(new GoalBuilder(second).getGoalImage());
    }

}
