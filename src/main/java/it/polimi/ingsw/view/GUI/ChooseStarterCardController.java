package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.controller.exceptions.UnexistentUserException;
import it.polimi.ingsw.controller.exceptions.WrongGamePhaseException;
import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.exceptions.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.GameDoesNotExistException;
import it.polimi.ingsw.model.exceptions.HandIsFullException;
import it.polimi.ingsw.view.View;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class ChooseStarterCardController {

    private View view;

    @FXML
    private Button front;
    @FXML
    private Button back;

    public void setView(View view){
        this.view = view;
    }

    public void playStarterCard(MouseEvent mouseEvent) {
        try {
            if(mouseEvent.getSource().equals(front)){
                view.chooseCardSide(CardSide.FRONT);
                return;
        }
        view.chooseCardSide(CardSide.BACK);
        }
        catch (IOException | EmptyDeckException | GameDoesNotExistException | HandIsFullException | UnexistentUserException | WrongGamePhaseException e) {
        throw new RuntimeException(e);
        }
    }
}
