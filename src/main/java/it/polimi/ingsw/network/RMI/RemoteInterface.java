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

public interface RemoteInterface extends Remote {

    void login(String username, ClientRemoteInterface client) throws NicknameAlreadyTakenException, IOException;

    void playCard(Integer GameID, String nickname, int handPos, Coords coords, CardSide side) throws IllegalActionException, NotYourTurnException, IllegalMoveException, GameDoesNotExistException, IOException, LobbyDoesNotExistException, UnexistentUserException;

    void createLobby(int numOfPlayers,String nickname) throws LobbyDoesNotExistException, RemoteException, CannotJoinMultipleLobbiesException, UnexistentUserException;

    void joinLobby(int lobbyID,String nickname) throws FullLobbyException, NicknameAlreadyTakenException, LobbyDoesNotExistException, IOException, CannotJoinMultipleLobbiesException, UnexistentUserException;

    void leaveLobby(int ID,String nickname) throws GameAlreadyStartedException, LobbyDoesNotExistException, RemoteException, GameDoesNotExistException, IOException, UnexistentUserException;

    void choosePawn(Integer lobbyID,String nickname, Pawn color) throws LobbyDoesNotExistException, PawnAlreadyTakenException, GameAlreadyStartedException, IOException, GameDoesNotExistException, UnexistentUserException;

    void sendMessage(Message message, int ID) throws LobbyDoesNotExistException, GameDoesNotExistException, RemoteException, PlayerChatMismatchException, UnexistentUserException;

    void chooseSecretGoal(Integer ID, String nickname, int goalID) throws IOException,IllegalGoalChosenException, WrongGamePhaseException, GameDoesNotExistException, UnexistentUserException;

    void drawCard(Integer gameID,String nickname, DeckTypeBox deckTypeBox) throws IllegalActionException, EmptyBufferException, NotYourTurnException, EmptyDeckException, GameDoesNotExistException, HandIsFullException, IOException, LobbyDoesNotExistException, UnexistentUserException;

    void chooseCardSide(Integer gameID,String nickname, CardSide side) throws IOException, EmptyDeckException, GameDoesNotExistException, HandIsFullException, UnexistentUserException, WrongGamePhaseException;

    void getCurrentStatus(String nickname) throws IOException;

    void disconnect(String nickname) throws RemoteException;

}
