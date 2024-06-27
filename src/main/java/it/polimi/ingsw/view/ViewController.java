package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.deck.DeckType;
import it.polimi.ingsw.model.deck.DeckTypeBox;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.game.Action;
import it.polimi.ingsw.model.game.GamePhase;
import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;

import java.util.Objects;

/**
 * Class ViewController
 * Controller class used to check if an action is allowed in the view
 */
public class ViewController {

    View view;

    /**
     * Class constructor
     * @param view Client view to associate this controller to
     */
    public ViewController(View view) { this.view = view; }

    /**
     * checks whether the user is connected to a lobby
     * @return boolean
     */
    public boolean userConnectedToLobby() {
        return view.getID() != null;
    }

    /**
     * checks whether the createLobby method can be called
     * @param numOfPlayers size of the lobby
     * @throws CannotJoinMultipleLobbiesException thrown if the user is already connected to a lobby
     */
    public void checkCreateLobby(int numOfPlayers) throws CannotJoinMultipleLobbiesException {

        // if the size is not 2, 3 or 4 players
        if (numOfPlayers < 2 || numOfPlayers > 4)
            throw new IllegalArgumentException();

        // if the user is already connected to any lobby
        if (userConnectedToLobby())
            throw new CannotJoinMultipleLobbiesException();
    }

    /**
     * checks whether the joinLobby method can be called
     * @param lobbyID ID of the lobby to join
     * @throws LobbyDoesNotExistException thrown if the specified ID doesn't correspond to any existing lobby
     * @throws FullLobbyException thrown if the selected lobby is already full
     * @throws CannotJoinMultipleLobbiesException thrown if the user is already connected to a lobby
     */
    public void checkJoinLobby(int lobbyID) throws LobbyDoesNotExistException, FullLobbyException, CannotJoinMultipleLobbiesException {

        // if the lobby doesn't exist
        if (!view.getLobbies().containsKey(lobbyID))
            throw new LobbyDoesNotExistException();

        // if the lobby is full
        if (view.getLobbies().get(lobbyID).getNumOfPlayers() ==
                view.getLobbies().get(lobbyID).getPlayers().size())
            throw new FullLobbyException();

        // if the user is already connected to any lobby
        if (userConnectedToLobby())
            throw new CannotJoinMultipleLobbiesException();
    }

    /**
     * checks whether the leaveLobby method can be called
     * @throws NotConnectedToLobbyException thrown if the user isn't connected to a lobby
     */
    public void checkLeaveLobby() throws NotConnectedToLobbyException {

        // if the user isn't connected to any lobby
        if (!userConnectedToLobby())
            throw new NotConnectedToLobbyException();
    }

    /**
     * checks whether the playCard method can be called
     * @param handPos index of the card to play
     * @param coords position to which the card should be played to
     * @throws NotYourTurnException thrown if playing a card when it's not the user's turn
     * @throws IllegalActionException thrown if playing a card when the user has already played one this turn
     * @throws IllegalCoordsException thrown if the user is trying to play a card on an illegal position
     * @throws RequirementsNotSatisfiedException thrown if the user is trying to play a Golden Card that doesn't have its requirements satisfied
     */
    public void checkPlayCard(int handPos, Coords coords) throws NotYourTurnException, IllegalActionException, IllegalCoordsException, RequirementsNotSatisfiedException {

        if (!view.isYourTurn())
            throw new NotYourTurnException();

        if (view.getAction() != Action.PLAY)
            throw new IllegalActionException();

        try {
            view.getMyField().checkIfPlaceable(coords);
            view.getMyField().checkRequirements((ResourceCard) view.getHand().getCard(handPos));
        } catch (IllegalCoordsException | RequirementsNotSatisfiedException e) {
            if(e.getClass().equals(OccupiedCoordsException.class)) { throw new OccupiedCoordsException(); }
            if(e.getClass().equals(UnreachablePositionException.class)) { throw new UnreachablePositionException(); }
            if(e.getClass().equals(RequirementsNotSatisfiedException.class)) { throw new RequirementsNotSatisfiedException(); }
        }
    }
}