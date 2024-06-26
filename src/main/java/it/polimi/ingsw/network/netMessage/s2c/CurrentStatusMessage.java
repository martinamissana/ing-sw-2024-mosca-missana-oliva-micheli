package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.network.netMessage.NetMessage;

import java.util.HashMap;

/**
 * Class CurrentStatusMessage
 * used to send to the views all the active lobbies created before their login
 */
public class CurrentStatusMessage extends NetMessage {
    private final HashMap<Integer, Lobby> lobbies;

    /**
     * Class constructor
     * @param lobbies the active lobbies
     */
    public CurrentStatusMessage(HashMap<Integer, Lobby> lobbies) {
        this.lobbies= new HashMap<>(lobbies);
    }

    /**
     * @return HashMap<Integer, Lobby>
     */
    public HashMap<Integer, Lobby> getLobbies() {
        return lobbies;
    }
}
