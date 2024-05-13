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
import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;

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

        boolean isInLobby = false;

        // if the user is connected to any lobby
        for (Lobby lobby : view.getLobbies().values())
            for (Player player : lobby.getPlayers())
                if (player.equals(view.getPlayer())) {
                    isInLobby = true;
                    break;
                }

        return isInLobby;
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
     * @throws LobbyDoesNotExistsException thrown if the specified ID doesn't correspond to any existing lobby
     * @throws FullLobbyException thrown if the selected lobby is already full
     * @throws CannotJoinMultipleLobbiesException thrown if the user is already connected to a lobby
     */
    public void checkJoinLobby(int lobbyID) throws LobbyDoesNotExistsException, FullLobbyException, CannotJoinMultipleLobbiesException {

        // if the lobby doesn't exist
        if (!view.getLobbies().containsKey(lobbyID))
            throw new LobbyDoesNotExistsException("Lobby doesn't exist");

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
     * checks whether the sendMessage method can be called
     * @param message message to send
     * @throws NotConnectedToLobbyException thrown if the user isn't connected to a lobby
     * @throws PlayerChatMismatchException thrown if the receiver isn't connected to the same lobby as the sender
     */
    public void checkSendMessage(Message message) throws NotConnectedToLobbyException, PlayerChatMismatchException {

        // if the user isn't the sender or if they're the receiver of the message
        if (!message.getSender().equals(view.getPlayer()) ||
                message.getReceiver().equals(view.getPlayer()))
            throw new RuntimeException("Invalid message sender/receiver");

        // if the user isn't connected to any lobby
        if (!userConnectedToLobby())
            throw new NotConnectedToLobbyException();

        // if the receiver isn't connected to the same lobby as the sender
        if (view.getLobbies().get(view.getID()).getPlayers().stream()       // the stream containing all the players in the
                .noneMatch(player -> player.equals(message.getReceiver()))) // sender's lobby doesn't contain the receiver
            throw new PlayerChatMismatchException();
    }

    /**
     * checks whether the choosePawn method can be called
     * @param color color of the desired pawn
     * @throws NotConnectedToLobbyException thrown if the user isn't connected to a lobby
     * @throws PawnAlreadyTakenException thrown if the user has already chosen a pawn, or if the selected pawn is already chosen by another user
     */
    public void checkChoosePawn(Pawn color) throws NotConnectedToLobbyException, PawnAlreadyTakenException {

        // if the user isn't connected to a lobby
        if (!userConnectedToLobby())
            throw new NotConnectedToLobbyException();

        // if the user has already selected a pawn
        if (view.getPawn() != null)
            throw new PawnAlreadyTakenException();

        // if the selected pawn is already taken by another user
        if (view.getLobbies().get(view.getID()).getPlayers().stream()
                .map(Player::getPawn)
                .anyMatch(p -> p.equals(color)))
            throw new PawnAlreadyTakenException();
    }

    /**
     * checks whether the chooseSecretGoal method can be called
     * @param goalID ID of the desired goal
     * @throws WrongGamePhaseException thrown if the game isn't on the "Choosing Secret Goal" phase
     */
    public void checkChooseSecretGoal(int goalID) throws WrongGamePhaseException {

        // if the game isn't in the right phase
        if (view.getGamePhase() != GamePhase.CHOOSING_SECRET_GOAL)
            throw new WrongGamePhaseException();

        // if the selected goal isn't one of the possible choices for the user
        if (view.getSecretGoalChoices().stream().map(Goal::getGoalID).noneMatch(ID -> ID == goalID))
            throw new IllegalArgumentException();
    }

    /**
     * checks whether the chooseCardSide method can be called
     * @throws WrongGamePhaseException thrown if the game isn't on the "Placing Starter Card" phase
     */
    public void checkChooseCardSide() throws WrongGamePhaseException {

        // if the game isn't in the right phase
        if (view.getGamePhase() != GamePhase.PLACING_STARTER_CARD)
            throw new WrongGamePhaseException();
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
            view.getPlayer().getField().checkIfPlaceable(coords);
            view.getPlayer().getField().checkRequirements((ResourceCard) view.getPlayer().getHand().getCard(handPos));
        } catch (IllegalCoordsException | RequirementsNotSatisfiedException e) {
            switch (e.getClass().getName()) {
                case "OccupiedCoordsException": { throw new OccupiedCoordsException(); }
                case "UnreachablePositionException": { throw new UnreachablePositionException(); }
                case "RequirementsNotSatisfiedException": { throw new RequirementsNotSatisfiedException(); }
            }
        }
    }

    /**
     * checks whether the drawCard method can be called
     * @param source source from which to draw the card
     * @throws NotYourTurnException thrown if drawing a card when it's not the user's turn
     * @throws IllegalActionException thrown if drawing a card when the user still has to play one this turn
     * @throws EmptyDeckException thrown if the selected deck is empty
     * @throws EmptyBufferException thrown if the selected deck buffer is empty
     */
    public void checkDrawCard(DeckTypeBox source) throws NotYourTurnException, IllegalActionException, EmptyDeckException, EmptyBufferException {

        if (!view.isYourTurn())
            throw new NotYourTurnException();

        if (view.getAction() != Action.DRAW || view.isLastRound())
            throw new IllegalActionException();

        switch (source) {
            case DeckType.RESOURCE -> { if (view.getTopResourceCard() == null) throw new EmptyDeckException("This deck is empty"); }
            case DeckType.GOLDEN ->  { if (view.getTopGoldenCard() == null) throw new EmptyDeckException("This deck is empty"); }
            case DeckBufferType.RES1 ->  { if (view.getDeckBuffers().get(DeckBufferType.RES1) == null) throw new EmptyBufferException("No card here"); }
            case DeckBufferType.RES2 ->  { if (view.getDeckBuffers().get(DeckBufferType.RES2) == null) throw new EmptyBufferException("No card here"); }
            case DeckBufferType.GOLD1 ->  { if (view.getDeckBuffers().get(DeckBufferType.GOLD1) == null) throw new EmptyBufferException("No card here"); }
            case DeckBufferType.GOLD2 ->  { if (view.getDeckBuffers().get(DeckBufferType.GOLD2) == null) throw new EmptyBufferException("No card here"); }
            default -> throw new IllegalArgumentException("Unexpected value: " + source);
        }
    }
}