package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.controller.exceptions.UnexistentUserException;
import it.polimi.ingsw.controller.exceptions.WrongGamePhaseException;
import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.model.exceptions.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.GameDoesNotExistException;
import it.polimi.ingsw.model.exceptions.HandIsFullException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

/**
 * Class ChooseStarterCardController
 * allows the player to choose the side of the starter card
 */
public class ChooseStarterCardController {
    private final ViewSingleton viewSingleton = ViewSingleton.getInstance();

    @FXML
    private Button front;
    @FXML
    private Button back;

    /**
     * sets the images of the started card sides
     */
    public void initialize() {
        StarterCard card = (StarterCard) viewSingleton.getView().getHand().getCard(0);
        front.setGraphic(new CardBuilder(card).getCardImage());
        card.flip();
        back.setGraphic(new CardBuilder(card).getCardImage());
        card.flip();
    }

    /**
     * chooses the side of the starter card
     * @param mouseEvent identifies the card side chosen
     */
    public void playStarterCard(MouseEvent mouseEvent) {
        try {
            if(mouseEvent.getSource().equals(front)) {
                viewSingleton.getView().chooseCardSide(CardSide.FRONT);
                return;
            }
            viewSingleton.getView().chooseCardSide(CardSide.BACK);
        }
        catch (IOException | EmptyDeckException | GameDoesNotExistException | HandIsFullException | UnexistentUserException | WrongGamePhaseException e) {
            throw new RuntimeException(e);
        }
    }
}
