package it.polimi.ingsw.network.RMI;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.model.deck.DeckTypeBox;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.model.player.Pawn;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface used from the client to communicate with the server
 */
public interface RemoteInterface extends Remote {

    /**
     * method used from the view to log in
     * @param username name of the player
     * @param client interface given to the server to communicate with the client
     * @throws NicknameAlreadyTakenException thrown when a user tries to log in with another user's nickname
     * @throws IOException general class of exceptions produced by failed or interrupted I/ O operations.
     */
    void login(String username, ClientRemoteInterface client) throws NicknameAlreadyTakenException, IOException;

    /**
     * method used from the view to log in
     * @param GameID the game ID
     * @param nickname the name of the player
     * @param handPos the position of the card in the hand
     * @param coords the coordinates where to place the card
     * @param side the side of the card
     * @throws IllegalActionException thrown when either playCard or drawCard are called when the game's Action state isn't set to PLAY or DRAW respectively
     * @throws NotYourTurnException thrown when a player tries to perform an action when it's not their turn
     * @throws IllegalMoveException thrown when violating the game's rules when placing a card
     * @throws GameDoesNotExistException thrown when trying to perform an action in a game that does not exist
     * @throws IOException throws IOException general class of exceptions produced by failed or interrupted I/ O operations
     * @throws LobbyDoesNotExistException thrown when trying to interact with a lobby that doesn't exist
     * @throws UnexistentUserException thrown if the user does not exist
     */
    void playCard(Integer GameID, String nickname, int handPos, Coords coords, CardSide side) throws IllegalActionException, NotYourTurnException, IllegalMoveException, GameDoesNotExistException, IOException, LobbyDoesNotExistException, UnexistentUserException;

    /**
     * method used from the view to create a lobby
     * @param numOfPlayers number of players in the lobby
     * @param nickname name of the player
     * @throws LobbyDoesNotExistException thrown when trying to interact with a lobby that doesn't exist
     * @throws RemoteException thrown if the client crashes
     * @throws CannotJoinMultipleLobbiesException thrown when a player tries to join a lobby while he is already in another lobby
     * @throws UnexistentUserException thrown if the user does not exist
     */
    void createLobby(int numOfPlayers,String nickname) throws LobbyDoesNotExistException, RemoteException, CannotJoinMultipleLobbiesException, UnexistentUserException;

    /**
     * method used from the view to join lobby
     * @param lobbyID the game ID
     * @param nickname name of the player
     * @throws FullLobbyException thrown when a user tries to join a lobby that's already at its declared maximum capacity of players
     * @throws NicknameAlreadyTakenException thrown when a user tries to log in with another user's nickname
     * @throws LobbyDoesNotExistException thrown when trying to interact with a lobby that doesn't exist
     * @throws IOException throws IOException general class of exceptions produced by failed or interrupted I/ O operations
     * @throws CannotJoinMultipleLobbiesException thrown when a player tries to join a lobby while he is already in another lobby
     * @throws UnexistentUserException thrown if the user does not exist
     */
    void joinLobby(int lobbyID,String nickname) throws FullLobbyException, NicknameAlreadyTakenException, LobbyDoesNotExistException, IOException, CannotJoinMultipleLobbiesException, UnexistentUserException;

    /**
     * method used from the view to leave a lobby
     * @param ID game ID
     * @param nickname name of the player
     * @throws GameAlreadyStartedException thrown when a user tries to perform lobby actions while the game is already started
     * @throws LobbyDoesNotExistException thrown when trying to interact with a lobby that doesn't exist
     * @throws GameDoesNotExistException thrown when trying to perform an action in a game that does not exist
     * @throws IOException throws IOException general class of exceptions produced by failed or interrupted I/ O operations
     * @throws UnexistentUserException thrown if the user does not exist
     */
    void leaveLobby(int ID,String nickname) throws GameAlreadyStartedException, LobbyDoesNotExistException, GameDoesNotExistException, IOException, UnexistentUserException;

    /**
     * method used from the view to choose the pawn color
     * @param lobbyID the game ID
     * @param nickname name of the player
     * @param color color chosen
     * @throws LobbyDoesNotExistException thrown when trying to interact with a lobby that doesn't exist
     * @throws PawnAlreadyTakenException thrown when the user selects a pawn that has already been chosen by another user in the lobby
     * @throws GameAlreadyStartedException thrown when a user tries to perform lobby actions while the game is already started
     * @throws IOException throws IOException general class of exceptions produced by failed or interrupted I/ O operations
     * @throws GameDoesNotExistException thrown when trying to perform an action in a game that does not exist
     * @throws UnexistentUserException thrown if the user does not exist
     */
    void choosePawn(Integer lobbyID,String nickname, Pawn color) throws LobbyDoesNotExistException, PawnAlreadyTakenException, GameAlreadyStartedException, IOException, GameDoesNotExistException, UnexistentUserException;

    /**
     * method used from the view to send a message in chat
     * @param message message sent
     * @param ID game ID
     * @throws LobbyDoesNotExistException thrown when trying to interact with a lobby that doesn't exist
     * @throws GameDoesNotExistException thrown when trying to perform an action in a game that does not exist
     * @throws RemoteException thrown if the server crashes
     * @throws PlayerChatMismatchException thrown if the sender and the receiver aren't in the same lobby
     * @throws UnexistentUserException thrown if the user does not exist
     */
    void sendMessage(Message message, int ID) throws LobbyDoesNotExistException, GameDoesNotExistException, RemoteException, PlayerChatMismatchException, UnexistentUserException;

    /**
     * method used from the view to choose the secret goal
     * @param ID the game ID
     * @param nickname name of the player
     * @param goalID ID of the chosen goal
     * @throws IOException throws IOException general class of exceptions produced by failed or interrupted I/ O operations
     * @throws IllegalGoalChosenException thrown when a user tries to choose a goal that is not one of the options
     * @throws WrongGamePhaseException thrown if the user tries to perform an action while the game phase is not correct
     * @throws GameDoesNotExistException thrown when trying to perform an action in a game that does not exist
     * @throws UnexistentUserException thrown if the user does not exist
     */
    void chooseSecretGoal(Integer ID, String nickname, int goalID) throws IOException,IllegalGoalChosenException, WrongGamePhaseException, GameDoesNotExistException, UnexistentUserException;

    /**
     * method used from the view to draw a card
     * @param gameID the game ID
     * @param nickname the name of the player
     * @param deckTypeBox the deck/deckBuffer used
     * @throws IllegalActionException thrown when either playCard or drawCard are called when the game's Action state isn't set to PLAY or DRAW respectively
     * @throws EmptyBufferException thrown when trying to draw a card from a deck buffer that couldn't refill because the corresponding deck is empty
     * @throws NotYourTurnException thrown when a player tries to perform an action when it's not their turn
     * @throws EmptyDeckException thrown when trying to draw a card from a deck that contains none
     * @throws GameDoesNotExistException thrown when trying to perform an action in a game that does not exist
     * @throws HandIsFullException thrown when a user tries to draw a card when their hand already contains three cards
     * @throws IOException throws IOException general class of exceptions produced by failed or interrupted I/ O operations
     * @throws LobbyDoesNotExistException thrown when trying to interact with a lobby that doesn't exist
     * @throws UnexistentUserException thrown if the user does not exist
     */
    void drawCard(Integer gameID,String nickname, DeckTypeBox deckTypeBox) throws IllegalActionException, EmptyBufferException, NotYourTurnException, EmptyDeckException, GameDoesNotExistException, HandIsFullException, IOException, LobbyDoesNotExistException, UnexistentUserException;

    /**
     * method used from the view to choose the side of the starter card
     * @param gameID the game ID
     * @param nickname name of the player
     * @param side side chosen
     * @throws IOException throws IOException general class of exceptions produced by failed or interrupted I/ O operations
     * @throws EmptyDeckException thrown when trying to draw a card from a deck that contains none
     * @throws GameDoesNotExistException thrown when trying to perform an action in a game that does not exist
     * @throws HandIsFullException thrown when a user tries to draw a card when their hand already contains three cards
     * @throws UnexistentUserException thrown if the user does not exist
     * @throws WrongGamePhaseException thrown if the user tries to perform an action while the game phase is not correct
     */
    void chooseCardSide(Integer gameID,String nickname, CardSide side) throws IOException, EmptyDeckException, GameDoesNotExistException, HandIsFullException, UnexistentUserException, WrongGamePhaseException;

    /**
     * method used from the view to get the active lobbies
     * @param nickname name of the player
     * @throws IOException throws IOException general class of exceptions produced by failed or interrupted I/ O operations
     */
    void getCurrentStatus(String nickname) throws IOException;

    /**
     * method used from the view to disconnect from the server
     * @param nickname name of the player
     * @throws RemoteException thrown if the server crashes
     */
    void disconnect(String nickname) throws RemoteException;

    /**
     * checks if the server is still active
     * @throws RemoteException thrown if the server crashes
     */
    void heartbeat() throws RemoteException;

}
