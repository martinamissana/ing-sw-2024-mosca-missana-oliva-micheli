package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.deck.DeckType;
import it.polimi.ingsw.model.deck.DeckTypeBox;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.RMI.ClientRemoteInterface;
import it.polimi.ingsw.network.RMI.RemoteInterface;
import it.polimi.ingsw.network.netMessage.HeartBeatMessage;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.DisconnectMessage;
import it.polimi.ingsw.network.netMessage.c2s.LobbyJoinedMessage;
import it.polimi.ingsw.network.netMessage.s2c.*;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class RMIView extends View implements ClientRemoteInterface , Serializable {

    private Registry registry;
    private final RemoteInterface RMIServer;

    public RMIView(RemoteInterface RMIServer) throws RemoteException, NotBoundException {
        super();
        this.RMIServer=RMIServer;
    }

    @Override
    public void login(String nickname) throws NicknameAlreadyTakenException, IOException {
        super.setPlayer(new Player(nickname));
        super.setNickname(nickname);
        RMIServer.login(nickname);
    }

    @Override
    public void createLobby(int numOfPlayers) throws LobbyDoesNotExistsException, IOException, CannotJoinMultipleLobbiesException, UnexistentUserException, FullLobbyException, NicknameAlreadyTakenException, ClassNotFoundException {
        RMIServer.createLobby(numOfPlayers, super.getPlayer().getNickname());
    }

    @Override
    public void joinLobby(int lobbyID) throws FullLobbyException, NicknameAlreadyTakenException, LobbyDoesNotExistsException, IOException, CannotJoinMultipleLobbiesException, UnexistentUserException {
        RMIServer.joinLobby(lobbyID, super.getPlayer().getNickname());
    }

    @Override
    public void leaveLobby() throws GameAlreadyStartedException, LobbyDoesNotExistsException, IOException, GameDoesNotExistException, UnexistentUserException {
        RMIServer.leaveLobby(super.getID(), super.getPlayer().getNickname());
    }

    @Override
    public void sendMessage(Message message) throws LobbyDoesNotExistsException, GameDoesNotExistException, UnexistentUserException, RemoteException, PlayerChatMismatchException {
        RMIServer.sendMessage(message,super.getID());
    }

    @Override
    public void choosePawn(Pawn color) throws LobbyDoesNotExistsException, PawnAlreadyTakenException, IOException, GameAlreadyStartedException, GameDoesNotExistException, UnexistentUserException {
        RMIServer.choosePawn(super.getID(), super.getPlayer().getNickname(), color);
    }

    @Override
    public void chooseSecretGoal(int goalID) throws IOException, IllegalGoalChosenException, WrongGamePhaseException, GameDoesNotExistException, UnexistentUserException {
        RMIServer.chooseSecretGoal(super.getID(), super.getPlayer().getNickname(), goalID);
    }

    @Override
    public void chooseCardSide(CardSide side) throws EmptyDeckException, GameDoesNotExistException, HandIsFullException, UnexistentUserException, IOException, WrongGamePhaseException {
        RMIServer.chooseCardSide(super.getID(), super.getPlayer().getNickname(), side);
    }

    @Override
    public void playCard(int handPos, Coords coords) throws IllegalActionException, NotYourTurnException, IllegalMoveException, GameDoesNotExistException, LobbyDoesNotExistsException, UnexistentUserException, IOException {
        RMIServer.playCard(super.getID(), super.getPlayer().getNickname(), handPos, coords);
    }

    @Override
    public void drawCard(DeckTypeBox deckTypeBox) throws IllegalActionException, EmptyBufferException, NotYourTurnException, EmptyDeckException, HandIsFullException, IOException, UnexistentUserException, LobbyDoesNotExistsException, GameDoesNotExistException {
        RMIServer.drawCard(super.getID(), super.getPlayer().getNickname(), deckTypeBox);
    }

    @Override
    public void flipCard(int handPos) throws UnexistentUserException, RemoteException, GameDoesNotExistException {
        RMIServer.flipCard(super.getID(),super.getPlayer().getNickname(), handPos);
        super.getHand().getCard(handPos).flip();
    }

    @Override
    public void getCurrentStatus() throws IOException {
        RMIServer.getCurrentStatus();
    }


    public void disconnect() throws IOException {
        Thread.currentThread().interrupt();
        notify(new DisconnectMessage());
    }

    public void heartbeat() throws RemoteException {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {
            try {
                RMIServer.heartbeat();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        };
        executor.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS);
    }

}
