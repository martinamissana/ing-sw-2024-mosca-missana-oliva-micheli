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
            while (true) {
                try {
                    heartbeat();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    /**
     * Sets nickname in the view and calls the method login of the server
     * @param nickname name of the player
     * @throws IOException general class of exceptions produced by failed or interrupted I/O operations
     */
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

    /**
     * calls the method createLobby of the server
     * @param  numOfPlayers players of the lobby
     * @throws LobbyDoesNotExistException thrown if the lobby doesn't exist
     * @throws IOException general class of exceptions produced by failed or interrupted I/O operations
     * @throws CannotJoinMultipleLobbiesException thrown if the player tries to join multiple lobbies
     * @throws UnexistentUserException thrown if the player doesn't exist
     * @throws FullLobbyException thrown if the lobby is full
     * @throws NicknameAlreadyTakenException thrown if the nickname is already taken
     * @throws ClassNotFoundException thrown if the class is not found
     */
    @Override
    public void createLobby(int numOfPlayers) throws LobbyDoesNotExistException, IOException, CannotJoinMultipleLobbiesException, UnexistentUserException, FullLobbyException, NicknameAlreadyTakenException, ClassNotFoundException {
        try {
            RMIServer.createLobby(numOfPlayers, super.getPlayer().getNickname());
        } catch (RemoteException e) {
            disconnect(super.getNickname());
        }
    }

    /**
     * calls the method joinLobby of the server
     * @param lobbyID ID of the lobby to join
     * @throws FullLobbyException thrown if the lobby is full
     * @throws LobbyDoesNotExistException thrown if the lobby does not exist
     * @throws IOException general class of exceptions produced by failed or interrupted I/O operations
     * @throws CannotJoinMultipleLobbiesException thrown if the player tries to join multiple lobbies
     * @throws UnexistentUserException thrown if the player doesn't exist
     */
    @Override
    public void joinLobby(int lobbyID) throws FullLobbyException, LobbyDoesNotExistException, IOException, CannotJoinMultipleLobbiesException, UnexistentUserException {
        try {
            RMIServer.joinLobby(lobbyID, super.getPlayer().getNickname());
        } catch (RemoteException | NicknameAlreadyTakenException e) {
            disconnect(super.getNickname());
        }
    }

    /**
     * calls the method leaveLobby of the server
     * @throws GameAlreadyStartedException thrown if the game is already started (the client can't leave the lobby)
     * @throws LobbyDoesNotExistException thrown if the lobby doesn't exist
     * @throws IOException general class of exceptions produced by failed or interrupted I/O operations
     * @throws GameDoesNotExistException thrown if the game doesn't exist
     * @throws UnexistentUserException thrown if the player doesn't exist
     */
    @Override
    public void leaveLobby() throws GameAlreadyStartedException, LobbyDoesNotExistException, IOException, GameDoesNotExistException, UnexistentUserException {
        try {
            RMIServer.leaveLobby(super.getID(), super.getPlayer().getNickname());
        } catch (RemoteException e) {
            disconnect(super.getNickname());
        }
    }

    /**
     * called if the player wants to send a message to someone (or everyone) in chat. It calls the method sendMessage of the server
     * @param message instance of Message class that contains sender, receiver and text of the message sent in chat
     * @throws LobbyDoesNotExistException thrown if the lobby does not exist
     * @throws GameDoesNotExistException thrown if the game does not exist
     * @throws UnexistentUserException thrown if the player does not exist
     * @throws RemoteException Exception thrown by every RMI method
     * @throws PlayerChatMismatchException thrown if sender or receiver
     */
    @Override
    public void sendMessage(Message message) throws LobbyDoesNotExistException, GameDoesNotExistException, UnexistentUserException, IOException, PlayerChatMismatchException {
        try{
            RMIServer.sendMessage(message,super.getID());
        }
        catch (RemoteException e){
            disconnect(super.getNickname());
        }
    }

    /**
     * called to select pawn color in the lobby. It calls the method of the server
     * @param color pawn color
     * @throws LobbyDoesNotExistException thrown if the lobby does not exist
     * @throws PawnAlreadyTakenException thrown if the color is already assigned to another user
     * @throws IOException general class of exceptions produced by failed or interrupted I/O operations
     * @throws GameAlreadyStartedException thrown if the game is already started
     * @throws GameDoesNotExistException thrown if the game does not exist
     * @throws UnexistentUserException thrown if the user does not exist
     */
    @Override
    public void choosePawn(Pawn color) throws LobbyDoesNotExistException, PawnAlreadyTakenException, IOException, GameAlreadyStartedException, GameDoesNotExistException, UnexistentUserException {
        try{
            RMIServer.choosePawn(super.getID(), super.getPlayer().getNickname(), color);
        }
        catch (RemoteException e){
            disconnect(super.getNickname());
        }
    }

    /**
     * called to select the secret goal from the two given goals. It calls the method of the server
     * @param goalID ID of the selected secret goal
     * @throws IOException general class of exceptions produced by failed or interrupted I/O operations
     * @throws IllegalGoalChosenException thrown if the chosen goal isn't one of the two given goals
     * @throws WrongGamePhaseException thrown if the phase of the game is wrong
     * @throws GameDoesNotExistException thrown if the game does not exist
     * @throws UnexistentUserException thrown if the user does not exist
     */
    @Override
    public void chooseSecretGoal(int goalID) throws IOException, IllegalGoalChosenException, WrongGamePhaseException, GameDoesNotExistException, UnexistentUserException {
        try{
            RMIServer.chooseSecretGoal(super.getID(), super.getPlayer().getNickname(), goalID);
        }
        catch (RemoteException e){
            disconnect(super.getNickname());
        }
    }

    /**
     * choose the side of the starter card to add it to the field, it calls the method of the server
     * @param side [FRONT-BACK] chosen side
     * @throws EmptyDeckException thrown if the deck is empty
     * @throws GameDoesNotExistException thrown if the game does not exist
     * @throws HandIsFullException thrown if the hand is full
     * @throws UnexistentUserException thrown if the user does not exist
     * @throws IOException general class of exceptions produced by failed or interrupted I/O operations
     * @throws WrongGamePhaseException thrown if the phase of the game is wrong
     */
    @Override
    public void chooseCardSide(CardSide side) throws EmptyDeckException, GameDoesNotExistException, HandIsFullException, UnexistentUserException, IOException, WrongGamePhaseException {
        try{
            RMIServer.chooseCardSide(super.getID(), super.getPlayer().getNickname(), side);
        }
        catch (RemoteException e){
            disconnect(super.getNickname());
        }
    }

    /**
     * called to add a card to the field, it calls the method of the server
     * @param handPos [0,1,2] the position in the hand of the card to be placed
     * @param coords coordinates where the card has to be placed
     * @throws IllegalActionException thrown when the method is called when the game's Action state isn't set to PLAY
     * @throws NotYourTurnException thrown when a player tries to perform an action when it's not their turn
     * @throws IllegalMoveException thrown when violating the game's rules when placing a card
     * @throws GameDoesNotExistException thrown when the game does not exist
     * @throws LobbyDoesNotExistException thrown when the lobby does not exist
     * @throws UnexistentUserException thrown when the user does not exist
     * @throws IOException general class of exceptions produced by failed or interrupted I/O operations
     */
    @Override
    public void playCard(int handPos, Coords coords, CardSide side) throws IllegalActionException, NotYourTurnException, IllegalMoveException, GameDoesNotExistException, LobbyDoesNotExistException, UnexistentUserException, IOException {
        RMIServer.playCard(super.getID(), super.getPlayer().getNickname(), handPos, coords, side);
    }

    /**
     * called to draw a card from a deckTypeBox, it calls the method of the server
     * @param deckTypeBox deck or deckBuffer where to draw
     * @throws IllegalActionException thrown when the method is called when the game's Action state isn't set to DRAW
     * @throws EmptyBufferException thrown where the deckBuffer is empty
     * @throws NotYourTurnException thrown if it is not the player turn
     * @throws EmptyDeckException thrown if the deck is empty
     * @throws HandIsFullException thrown if the hand is full
     * @throws IOException general class of exceptions produced by failed or interrupted I/O operations
     * @throws UnexistentUserException thrown if the user does not exist
     * @throws LobbyDoesNotExistException thrown if the lobby does not exist
     * @throws GameDoesNotExistException thrown if the game does not exist
     */
    @Override
    public void drawCard(DeckTypeBox deckTypeBox) throws IllegalActionException, EmptyBufferException, NotYourTurnException, EmptyDeckException, HandIsFullException, IOException, UnexistentUserException, LobbyDoesNotExistException, GameDoesNotExistException {
        RMIServer.drawCard(super.getID(), super.getPlayer().getNickname(), deckTypeBox);
    }


    /**
     * used to update a new client on the current status of the lobbies, it calls the method of the server
     * @throws IOException general class of exceptions produced by failed or interrupted I/O operations
     */
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
