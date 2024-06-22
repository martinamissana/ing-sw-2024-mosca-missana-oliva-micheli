package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.controller.exceptions.IllegalGoalChosenException;
import it.polimi.ingsw.controller.exceptions.UnexistentUserException;
import it.polimi.ingsw.controller.exceptions.WrongGamePhaseException;
import it.polimi.ingsw.model.exceptions.GameDoesNotExistException;
import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.view.View;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class SecretGoalController {
    private View view;
    private Goal first;
    private Goal second;

    @FXML
    Pane firstGoal;
    @FXML
    Pane secondGoal;

    @FXML
    public void chooseGoal(MouseEvent mouseEvent){
        if(mouseEvent.getSource().equals(firstGoal)){
            try {
                view.chooseSecretGoal(first.getGoalID());
            } catch (IOException | IllegalGoalChosenException | WrongGamePhaseException | GameDoesNotExistException |
                     UnexistentUserException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if(mouseEvent.getSource().equals(secondGoal)){
            try {
                view.chooseSecretGoal(second.getGoalID());
            } catch (IOException | IllegalGoalChosenException | WrongGamePhaseException | GameDoesNotExistException |
                     UnexistentUserException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public  void setView(View view){
        this.view=view;
        this.first = view.getSecretGoalChoices().getFirst();
        this.second = view.getSecretGoalChoices().get(1);
        firstGoal.getChildren().add(new GoalBuilder(first).getGoalImage());
        secondGoal.getChildren().add(new GoalBuilder(second).getGoalImage());
    }


}
