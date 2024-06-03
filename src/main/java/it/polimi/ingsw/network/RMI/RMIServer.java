package it.polimi.ingsw.network.RMI;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.model.deck.DeckTypeBox;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer extends UnicastRemoteObject implements RemoteInterface {
    private final Controller c;

    public RMIServer(Controller c) throws RemoteException {
      this.c=c;
    }

    @Override
    public void login(String username, ClientRemoteInterface client) throws NicknameAlreadyTakenException, IOException {
        new RMIVirtualView(c, client, username);
        c.login(username);
        new Thread(() -> {
            try {
                ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                String nickname = client.getNickname();
                Runnable task = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            client.heartbeat();
                        } catch (RemoteException e) {
                            disconnect(nickname);
                        }
                    }
                };
                executor.scheduleAtFixedRate(task, 0, 3, TimeUnit.SECONDS);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

        }).start();

    }

    @Override
    public void playCard(Integer GameID, String nickname, int handPos, Coords coords, CardSide side) throws IllegalActionException, NotYourTurnException, IllegalMoveException, GameDoesNotExistException, IOException, LobbyDoesNotExistsException, UnexistentUserException {
        c.playCard(GameID,nickname,handPos,coords,side);
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
    public void chooseSecretGoal(Integer ID, String nickname, int goalID) throws IOException, IllegalGoalChosenException, WrongGamePhaseException, GameDoesNotExistException, UnexistentUserException {
        c.chooseSecretGoal(ID,nickname,goalID);
    }


    @Override
    public void drawCard(Integer gameID,String nickname, DeckTypeBox deckTypeBox) throws IllegalActionException, EmptyBufferException, NotYourTurnException, EmptyDeckException, GameDoesNotExistException, HandIsFullException, IOException, LobbyDoesNotExistsException, UnexistentUserException {
        c.drawCard(gameID,nickname,deckTypeBox);
    }

    @Override
    public void chooseCardSide(Integer gameID,String nickname, CardSide side) throws IOException, EmptyDeckException, GameDoesNotExistException, HandIsFullException, UnexistentUserException, WrongGamePhaseException {
        c.chooseCardSide(gameID,nickname,side);
    }

    @Override
    public void flipCard(Integer gameID,String nickname, int handPos) throws GameDoesNotExistException, RemoteException, UnexistentUserException {
        c.flipCard(gameID,nickname,handPos);
    }

    @Override
    public void getCurrentStatus(String nickname) throws IOException {
        c.getCurrentStatus(nickname);
    }

    @Override
    public void disconnect(String nickname) {
        Integer ID=null;
        for(Lobby l: c.getGh().getLobbies().values()){
            for(Player p: l.getPlayers()){
                if(p.getNickname().equals(nickname)){
                    ID=l.getID();
                }
            }
        }
        try {
                if (ID != null && nickname != null) {
                    c.leaveLobby(nickname, ID);
                }
            } catch (LobbyDoesNotExistsException | GameDoesNotExistException | IOException |
                     UnexistentUserException ignored) {
            }
            c.getGh().removeUser(nickname);
        try {
            c.disconnect(nickname,ID);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
