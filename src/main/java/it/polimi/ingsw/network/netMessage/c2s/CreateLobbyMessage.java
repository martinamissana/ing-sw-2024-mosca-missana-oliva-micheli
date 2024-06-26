package it.polimi.ingsw.network.netMessage.c2s;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

/**
 * Class CreateLobbyMessage
 * used to request the server to create a lobby
 */
public class CreateLobbyMessage extends NetMessage {
    private final Player creator;
    private final int numOfPlayers;

    /**
     * Class constructor
     * @param numOfPlayers the number of players in the lobby
     * @param creator the player who wants to create the lobby
     */
    public CreateLobbyMessage(int numOfPlayers, Player creator) {
        this.creator = creator;
        this.numOfPlayers=numOfPlayers;
    }

    /**
     * @return Player
     */
    public Player getCreator() {
        return creator;
    }

    /**
     * @return int
     */
    public int getNumOfPlayers() {
        return numOfPlayers;
    }
}
