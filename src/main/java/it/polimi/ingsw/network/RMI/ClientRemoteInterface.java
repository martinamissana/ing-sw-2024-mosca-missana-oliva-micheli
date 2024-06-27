package it.polimi.ingsw.network.RMI;

import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.HandIsFullException;
import it.polimi.ingsw.model.exceptions.IllegalMoveException;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.network.netMessage.NetMessage;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface used by the server to communicate with the views
 */
public interface ClientRemoteInterface extends Remote {

    /**
     * method used to send messages from server to client
     * @param message the message sent
     * @throws IOException general class of exceptions produced by failed or interrupted I/ O operations.
     * @throws FullLobbyException thrown when a user tries to join a lobby that's already at its declared maximum capacity of players
     * @throws NicknameAlreadyTakenException thrown when a user tries to log in with another user's nickname
     * @throws HandIsFullException thrown when a user tries to draw a card when their hand already contains three cards
     * @throws IllegalMoveException thrown when violating the game's rules when placing a card
     */
    void elaborate(NetMessage message) throws IOException, FullLobbyException, NicknameAlreadyTakenException, HandIsFullException, IllegalMoveException;

    /**
     * method used to check if the client is still active
     * @throws RemoteException thrown if the client crashes
     */
    void heartbeat() throws RemoteException;

    /**
     * @return the name of the player
     * @throws RemoteException thrown if the client crashes
     */
    String getNickname() throws RemoteException;
    }
