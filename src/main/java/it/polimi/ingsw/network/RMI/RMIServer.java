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

/**
 * RMIServer class
 * implements the remote interface the client uses to communicate with the server in RMI
 */
public class RMIServer extends UnicastRemoteObject implements RemoteInterface {
    private final Controller c;

    /**
     * Class constructor
     * @param c controller
     * @throws RemoteException thrown if the server crashes
     */
    public RMIServer(Controller c) throws RemoteException {
      this.c=c;
    }

    /**
     * login method,
     * it creates the virtual view for that client,
     * it calls the method login in controller,
     * it starts a thread to check if the client is present calling the client method heartbeat every 3 seconds,
     * @param username nickname of the client
     * @param client Client remote interface used by the server to send messages to the client
     * @throws NicknameAlreadyTakenException thrown if a user with that nickname is already present
     * @throws IOException general class of exceptions produced by failed or interrupted I/ O operations.
     */
    @Override
    public void login(String username, ClientRemoteInterface client) throws NicknameAlreadyTakenException, IOException {
        new RMIVirtualView(c, client, username);
        c.login(username);
        new Thread(() -> {
            try {
                ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                String nickname = client.getNickname();
                Runnable task = () -> {
                    try {
                        client.heartbeat();
                    } catch (RemoteException e) {
                        Thread.currentThread().interrupt();
                        disconnect(nickname);
                        executor.shutdown();
                    }
                };
                executor.scheduleAtFixedRate(task, 0, 3, TimeUnit.SECONDS);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

        }).start();

    }

    /**
     * method used to play a card in the field, it calls the method in controller
     * @param GameID ID of the game
     * @param nickname name of the user
     * @param handPos position in the user hand of the card
     * @param coords coordinates where to place the card at
     * @param side side chosen of the card
     * @throws IllegalActionException  thrown when playCard is called when the game's Action state isn't set to PLAY
     * @throws NotYourTurnException  thrown when a player tries to perform an action when it's not their turn
     * @throws IllegalMoveException  thrown when violating the game's rules when placing a card
     * @throws GameDoesNotExistException thrown when the game does not exist
     * @throws IOException general class of exceptions produced by failed or interrupted I/ O operations.
     * @throws LobbyDoesNotExistException thrown if the lobby does not exist
     * @throws UnexistentUserException thrown if the user does not exist
     */
    @Override
    public void playCard(Integer GameID, String nickname, int handPos, Coords coords, CardSide side) throws IllegalActionException, NotYourTurnException, IllegalMoveException, GameDoesNotExistException, IOException, LobbyDoesNotExistException, UnexistentUserException {
        c.playCard(GameID,nickname,handPos,coords,side);
    }

    /**
     * method used to create a lobby, it calls the controller method
     * @param numOfPlayers number of players in the lobby
     * @param nickname name of the user
     * @throws LobbyDoesNotExistException thrown if the lobby does not exist
     * @throws RemoteException thrown if the server crashes
     * @throws UnexistentUserException thrown if the user does not exist
     * @throws CannotJoinMultipleLobbiesException thrown if the user is already in another lobby
     */
    @Override
    public void createLobby(int numOfPlayers,String nickname) throws LobbyDoesNotExistException, RemoteException, UnexistentUserException, CannotJoinMultipleLobbiesException {
        c.createLobby(numOfPlayers,nickname);
    }

    /**
     * method used to join a lobby, it calls the controller method
     * @param lobbyID ID of the lobby to join
     * @param nickname name of the user
     * @throws FullLobbyException thrown if the lobby is already full
     * @throws LobbyDoesNotExistException thrown if the lobby does not exist
     * @throws IOException general class of exceptions produced by failed or interrupted I/ O operations.
     * @throws CannotJoinMultipleLobbiesException thrown if the user is already in another lobby
     * @throws UnexistentUserException thrown if the lobby does not exist
     */
    @Override
    public void joinLobby(int lobbyID,String nickname) throws FullLobbyException, LobbyDoesNotExistException, IOException, CannotJoinMultipleLobbiesException, UnexistentUserException {
        c.joinLobby(nickname,lobbyID);
    }

    /**
     * method used to leave the lobby
     * @param lobbyID ID of the lobby
     * @param nickname name of the user
     * @throws GameAlreadyStartedException thrown if the game is already started
     * @throws LobbyDoesNotExistException thrown if the lobby does not exist
     * @throws IOException general class of exceptions produced by failed or interrupted I/ O operations.
     * @throws GameDoesNotExistException thrown if the game does not exist
     * @throws UnexistentUserException thrown if the user does not exist
     */
    @Override
    public void leaveLobby(int lobbyID,String nickname) throws GameAlreadyStartedException, LobbyDoesNotExistException, IOException, GameDoesNotExistException, UnexistentUserException {
        c.leaveLobby(nickname,lobbyID);
    }

    /**
     * method used to choose the pawn color, it calls the controller method
     * @param lobbyID ID of the lobby
     * @param nickname name of te user
     * @param color color chosen
     * @throws LobbyDoesNotExistException thrown if the lobby does not exist
     * @throws PawnAlreadyTakenException thrown if the pawn color is already chosen by someone else
     * @throws GameAlreadyStartedException thrown if the game is already started
     * @throws IOException general class of exceptions produced by failed or interrupted I/ O operations.
     * @throws GameDoesNotExistException thrown if the game does not exist
     * @throws UnexistentUserException thrown if the user does not exist
     */
    @Override
    public void choosePawn(Integer lobbyID,String nickname,Pawn color) throws LobbyDoesNotExistException, PawnAlreadyTakenException, GameAlreadyStartedException, IOException, GameDoesNotExistException, UnexistentUserException {
        c.choosePawn(lobbyID,nickname,color);
    }

    /**
     * method used to send a message in chat, it calls the controller method
     * @param message it contains sender-receiver-text of the message
     * @param ID ID  of the lobby/game
     * @throws LobbyDoesNotExistException thrown if the lobby does not exist
     * @throws GameDoesNotExistException thrown if the game does not exist
     * @throws RemoteException thrown if the server crashes
     * @throws PlayerChatMismatchException thrown if the chat is global but the receiver isn't null
     * @throws UnexistentUserException thrown if the user does not exist
     */
    @Override
    public void sendMessage(Message message, int ID) throws LobbyDoesNotExistException, GameDoesNotExistException, RemoteException, PlayerChatMismatchException, UnexistentUserException {
        c.send(message,ID);
    }

    /**
     * method used to choose the player secret goal, it calls the controller method
     * @param ID ID of the game
     * @param nickname name of the user
     * @param goalID ID of the chosen goal
     * @throws IOException general class of exceptions produced by failed or interrupted I/ O operations.
     * @throws IllegalGoalChosenException thrown if the ID of the goal isn't a choosable goal
     * @throws WrongGamePhaseException thrown if the phase of the game is wrong
     * @throws GameDoesNotExistException thrown if the game does not exist
     * @throws UnexistentUserException thrown if the user does not exist
     */
    @Override
    public void chooseSecretGoal(Integer ID, String nickname, int goalID) throws IOException, IllegalGoalChosenException, WrongGamePhaseException, GameDoesNotExistException, UnexistentUserException {
        c.chooseSecretGoal(ID,nickname,goalID);
    }


    /**
     * method used to draw a card, it calls the controller method
     * @param gameID ID of the game
     * @param nickname name of the user
     * @param deckTypeBox source from where the client wants to draw
     * @throws IllegalActionException  thrown when drawCard is called when the game's Action state isn't set to DRAW
     * @throws EmptyBufferException thrown if the DeckBuffer is empty
     * @throws NotYourTurnException  thrown when a player tries to perform an action when it's not their turn
     * @throws EmptyDeckException thrown if the deck is empty
     * @throws GameDoesNotExistException thrown if the game does not exist
     * @throws HandIsFullException thrown if the hand is full
     * @throws IOException general class of exceptions produced by failed or interrupted I/ O operations.
     * @throws LobbyDoesNotExistException thrown if the lobby does not exist
     * @throws UnexistentUserException thrown if the user does not exist
     */
    @Override
    public void drawCard(Integer gameID,String nickname, DeckTypeBox deckTypeBox) throws IllegalActionException, EmptyBufferException, NotYourTurnException, EmptyDeckException, GameDoesNotExistException, HandIsFullException, IOException, LobbyDoesNotExistException, UnexistentUserException {
        c.drawCard(gameID,nickname,deckTypeBox);
    }

    /**
     * method to choose the card side for the starter card, it calls the controller method
     * @param gameID ID of the game
     * @param nickname name of the user
     * @param side chosen side of the starter card
     * @throws IOException general class of exceptions produced by failed or interrupted I/ O operations.
     * @throws EmptyDeckException thrown if the deck is empty
     * @throws GameDoesNotExistException thrown if the game does not exist
     * @throws HandIsFullException thrown if the hand is full
     * @throws UnexistentUserException thrown if the user does not exist
     * @throws WrongGamePhaseException thrown if the game phase is wrong
     */
    @Override
    public void chooseCardSide(Integer gameID,String nickname, CardSide side) throws IOException, EmptyDeckException, GameDoesNotExistException, HandIsFullException, UnexistentUserException, WrongGamePhaseException {
        c.chooseCardSide(gameID,nickname,side);
    }

    /**
     * method used to inform the client on the present lobbies
     * @param nickname name of the user
     * @throws IOException general class of exceptions produced by failed or interrupted I/ O operations.
     */
    @Override
    public void getCurrentStatus(String nickname) throws IOException {
        c.getCurrentStatus(nickname);
    }

    /**
     * method used to remove the client from the users if the client crashes
     * @param nickname name of the user
     */
    @Override
    public void disconnect(String nickname) {
        Integer ID = null;
        for (Lobby l : c.getGh().getLobbies().values()) {
            for (Player p : l.getPlayers()) {
                if (p.getNickname().equals(nickname)) {
                    ID = l.getID();
                    break;
                }
            }
        }
        try {
            if (ID != null) {
                c.leaveLobby(nickname, ID);
            }
        } catch (LobbyDoesNotExistException | GameDoesNotExistException | IOException |
                 UnexistentUserException ignored) {
        }
        c.getGh().removeUser(nickname);
        try {
            c.disconnect(nickname, ID);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
