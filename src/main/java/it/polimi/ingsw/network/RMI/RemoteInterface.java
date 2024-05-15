package it.polimi.ingsw.network.RMI;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.model.deck.DeckTypeBox;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.view.RMIView;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInterface extends Remote {

    void connect(ClientRemoteInterface client) throws RemoteException, NotBoundException; //send to the server a clientremoteinterface in order to send messages from server to client

    void login(String username) throws NicknameAlreadyTakenException, IOException;

    void heartbeat() throws RemoteException;

    void playCard(Integer GameID, String nickname, int handPos, Coords coords) throws IllegalActionException, NotYourTurnException, IllegalMoveException, GameDoesNotExistException, IOException, LobbyDoesNotExistsException, UnexistentUserException;

    void createLobby(int numOfPlayers,String nickname) throws LobbyDoesNotExistsException, RemoteException, CannotJoinMultipleLobbiesException, UnexistentUserException;

    void joinLobby(int lobbyID,String nickname) throws FullLobbyException, NicknameAlreadyTakenException, LobbyDoesNotExistsException, IOException, CannotJoinMultipleLobbiesException, UnexistentUserException;

    void leaveLobby(int ID,String nickname) throws GameAlreadyStartedException, LobbyDoesNotExistsException, RemoteException, GameDoesNotExistException, IOException, UnexistentUserException;

    void choosePawn(Integer lobbyID,String nickname, Pawn color) throws LobbyDoesNotExistsException, PawnAlreadyTakenException, GameAlreadyStartedException, IOException, GameDoesNotExistException, UnexistentUserException;

    void sendMessage(Message message, int ID) throws LobbyDoesNotExistsException, GameDoesNotExistException, RemoteException, PlayerChatMismatchException, UnexistentUserException;

    void chooseSecretGoal(Integer ID, String nickname, int goalID) throws IOException,IllegalGoalChosenException, WrongGamePhaseException, GameDoesNotExistException, UnexistentUserException;

    void drawCard(Integer gameID,String nickname, DeckTypeBox deckTypeBox) throws IllegalActionException, EmptyBufferException, NotYourTurnException, EmptyDeckException, GameDoesNotExistException, HandIsFullException, IOException, LobbyDoesNotExistsException, UnexistentUserException;

    void chooseCardSide(Integer gameID,String nickname, CardSide side) throws IOException, EmptyDeckException, GameDoesNotExistException, HandIsFullException, UnexistentUserException, WrongGamePhaseException;

    void flipCard(Integer gameID,String nickname, int handPos) throws GameDoesNotExistException,RemoteException,UnexistentUserException;

    void getCurrentStatus() throws IOException;

}
