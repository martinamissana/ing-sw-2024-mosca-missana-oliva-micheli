package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.model.deck.DeckTypeBox;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.RMI.RemoteInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class RMIView extends View {

    private final Registry registry;
    private final RemoteInterface RMIServer;

    public RMIView() throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry();
        System.out.println("RMI registry bindings: ");
        String[] e = registry.list();
        for (String string : e) {
            System.out.println(string);
        }
        String remoteObjectName = "RMIServer";
        RMIServer = (RemoteInterface) registry.lookup(remoteObjectName);
    }

    public void login(String nickname) throws NicknameAlreadyTakenException {
        super.setPlayer(new Player(nickname));
        super.setNickname(nickname);
        System.out.println("you tried to login");
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
    public void leaveGame() throws LobbyDoesNotExistsException, GameDoesNotExistException, UnexistentUserException, IOException {
        RMIServer.leaveGame(super.getID(), super.getPlayer().getNickname());
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
    }

    @Override
    public void getCurrentStatus() throws IOException, FullLobbyException, NicknameAlreadyTakenException, ClassNotFoundException {
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
