package it.polimi.ingsw.network.RMI;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.model.deck.DeckTypeBox;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.model.player.Pawn;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer extends UnicastRemoteObject implements RemoteInterface,Runnable {
    private final Controller c;
    public RMIServer(Controller c) throws RemoteException {
      this.c=c;

    }

    @Override
    public void login(String username) throws NicknameAlreadyTakenException, IOException {
        c.login(username);
    }

    @Override
    public void heartbeat() throws RemoteException {

    }

    @Override
    public void playCard(Integer GameID, String nickname, int handPos, Coords coords) throws IllegalActionException, NotYourTurnException, IllegalMoveException, GameDoesNotExistException, IOException, LobbyDoesNotExistsException, UnexistentUserException {
        c.playCard(GameID,nickname,handPos,coords);
    }

    @Override
    public void createLobby(int numOfPlayers,String nickname) throws LobbyDoesNotExistsException, RemoteException, UnexistentUserException, CannotJoinMultipleLobbiesException {
        c.createLobby(numOfPlayers,nickname);
    }

    @Override
    public void joinLobby(int lobbyID,String nickname) throws FullLobbyException, NicknameAlreadyTakenException, LobbyDoesNotExistsException, IOException, CannotJoinMultipleLobbiesException, UnexistentUserException {
        c.joinLobby(nickname,lobbyID);
    }

    @Override
    public void leaveLobby(int lobbyID,String nickname) throws GameAlreadyStartedException, LobbyDoesNotExistsException, IOException, GameDoesNotExistException, UnexistentUserException {
        c.leaveLobby(nickname,lobbyID);
    }

    @Override
    public void choosePawn(Integer lobbyID,String nickname,Pawn color) throws LobbyDoesNotExistsException, PawnAlreadyTakenException, GameAlreadyStartedException, IOException, GameDoesNotExistException, UnexistentUserException {
        c.choosePawn(lobbyID,nickname,color);
    }

    @Override
    public void sendMessage(Message message, int ID) throws LobbyDoesNotExistsException, GameDoesNotExistException, RemoteException, PlayerChatMismatchException, UnexistentUserException {
        c.send(message,ID);
    }

    @Override
    public void choosePersonalGoal(Integer ID,String nickname, int goalID) throws RemoteException, IllegalGoalChosenException, WrongGamePhaseException, GameDoesNotExistException, UnexistentUserException {
        c.choosePersonalGoal(ID,nickname,goalID);
    }

    @Override
    public void leaveGame(Integer gameID,String nickname) throws LobbyDoesNotExistsException, GameDoesNotExistException, IOException, UnexistentUserException {
        c.leaveGame(gameID,nickname);
    }

    @Override
    public void drawCard(Integer gameID,String nickname, DeckTypeBox deckTypeBox) throws IllegalActionException, EmptyBufferException, NotYourTurnException, EmptyDeckException, GameDoesNotExistException, HandIsFullException, IOException, LobbyDoesNotExistsException, UnexistentUserException {
        c.drawCard(gameID,nickname,deckTypeBox);
    }

    @Override
    public void chooseCardSide(Integer gameID,String nickname, CardSide side) throws IOException, EmptyDeckException, GameDoesNotExistException, HandIsFullException, UnexistentUserException {
        c.chooseCardSide(gameID,nickname,side);
    }

    @Override
    public void flipCard(Integer gameID,String nickname, int handPos) throws GameDoesNotExistException, RemoteException, UnexistentUserException {
        c.flipCard(gameID,nickname,handPos);
    }

    @Override
    public void run() {

    }
}
