package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.model.deck.DeckTypeBox;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.RMI.ClientRemoteInterface;
import it.polimi.ingsw.network.RMI.RemoteInterface;
import it.polimi.ingsw.network.netMessage.c2s.DisconnectMessage;
import it.polimi.ingsw.network.netMessage.s2c.LoginFail_NicknameAlreadyTaken;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.io.IOException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * RMIView class
 * the methods represent the action that the client can do
 */
public class RMIView extends View implements ClientRemoteInterface , Serializable {

    private final RemoteInterface RMIServer;

    /**
     * Class constructor
     * @param RMIServer Server interface, allows client to call the server methods
     * @throws RemoteException Exception thrown by every RMI method
     * @throws NotBoundException thrown if an attempt is made to lookup or unbind in the registry a name that has no associated binding.
     */
    public RMIView(RemoteInterface RMIServer) throws RemoteException, NotBoundException {
        super();
        this.RMIServer=RMIServer;
        new Thread(() -> {
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            Runnable task = () -> {
                try {
                    RMIServer.heartbeat();
                } catch (RemoteException e) {
                    Thread.currentThread().interrupt();
                    try {
                        disconnect(super.getNickname());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    executor.shutdown();
                }
            };
            executor.scheduleAtFixedRate(task, 0, 3, TimeUnit.SECONDS);

        }).start();
    }

    @Override
    public void login(String nickname) throws IOException {
        super.setPlayer(new Player(nickname));
        super.setNickname(nickname);
        try {
            RMIServer.login(nickname,(ClientRemoteInterface) UnicastRemoteObject.exportObject(this, 0));
        } catch (NicknameAlreadyTakenException e) {
            notify(new LoginFail_NicknameAlreadyTaken());
        } catch (RemoteException e){
            disconnect(super.getNickname());
        }
    }

    @Override
    public void createLobby(int numOfPlayers) throws LobbyDoesNotExistException, IOException, CannotJoinMultipleLobbiesException, UnexistentUserException, FullLobbyException, NicknameAlreadyTakenException, ClassNotFoundException {
        try {
            RMIServer.createLobby(numOfPlayers, super.getPlayer().getNickname());
        } catch (RemoteException e) {
            disconnect(super.getNickname());
        }
    }

    @Override
    public void joinLobby(int lobbyID) throws FullLobbyException, LobbyDoesNotExistException, IOException, CannotJoinMultipleLobbiesException, UnexistentUserException {
        try {
            RMIServer.joinLobby(lobbyID, super.getPlayer().getNickname());
        } catch (RemoteException | NicknameAlreadyTakenException e) {
            disconnect(super.getNickname());
        }
    }

    @Override
    public void leaveLobby() throws GameAlreadyStartedException, LobbyDoesNotExistException, IOException, GameDoesNotExistException, UnexistentUserException {
        try {
            RMIServer.leaveLobby(super.getID(), super.getPlayer().getNickname());
        } catch (RemoteException e) {
            disconnect(super.getNickname());
        }
    }

    @Override
    public void sendMessage(Message message) throws LobbyDoesNotExistException, GameDoesNotExistException, UnexistentUserException, IOException, PlayerChatMismatchException {
        try{
            RMIServer.sendMessage(message,super.getID());
        }
        catch (RemoteException e){
            disconnect(super.getNickname());
        }
    }

    @Override
    public void choosePawn(Pawn color) throws LobbyDoesNotExistException, PawnAlreadyTakenException, IOException, GameAlreadyStartedException, GameDoesNotExistException, UnexistentUserException {
        try{
            RMIServer.choosePawn(super.getID(), super.getPlayer().getNickname(), color);
        }
        catch (RemoteException e){
            disconnect(super.getNickname());
        }
    }

    @Override
    public void chooseSecretGoal(int goalID) throws IOException, IllegalGoalChosenException, WrongGamePhaseException, GameDoesNotExistException, UnexistentUserException {
        try{
            RMIServer.chooseSecretGoal(super.getID(), super.getPlayer().getNickname(), goalID);
        }
        catch (RemoteException e){
            disconnect(super.getNickname());
        }
    }

    @Override
    public void chooseCardSide(CardSide side) throws EmptyDeckException, GameDoesNotExistException, HandIsFullException, UnexistentUserException, IOException, WrongGamePhaseException {
        try{
            RMIServer.chooseCardSide(super.getID(), super.getPlayer().getNickname(), side);
        }
        catch (RemoteException e){
            disconnect(super.getNickname());
        }
    }

    @Override
    public void playCard(int handPos, Coords coords, CardSide side) throws IllegalActionException, NotYourTurnException, IllegalMoveException, GameDoesNotExistException, LobbyDoesNotExistException, UnexistentUserException, IOException {
        try{
            RMIServer.playCard(super.getID(), super.getPlayer().getNickname(), handPos, coords, side);
        }
        catch (RemoteException e){
            disconnect(super.getNickname());
        }
    }

    @Override
    public void drawCard(DeckTypeBox deckTypeBox) throws IllegalActionException, EmptyBufferException, NotYourTurnException, EmptyDeckException, HandIsFullException, IOException, UnexistentUserException, LobbyDoesNotExistException, GameDoesNotExistException {
        try {
            RMIServer.drawCard(super.getID(), super.getPlayer().getNickname(), deckTypeBox);
        } catch (RemoteException e) {
            disconnect(super.getNickname());
        }
    }

    @Override
    public void getCurrentStatus() throws IOException {
        try{
            RMIServer.getCurrentStatus(super.getNickname());
        }
        catch (RemoteException e){
            disconnect(super.getNickname());
        }
    }

    @Override
    public void disconnect(String nickname) throws IOException {
        try {
            RMIServer.disconnect(nickname);
        } catch (RemoteException e){
            if (Objects.equals(super.getNickname(), nickname)) notify(new DisconnectMessage());
        }
    }

    @Override
    public void heartbeat() throws RemoteException {
    }

}
