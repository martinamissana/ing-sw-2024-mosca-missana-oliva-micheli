package it.polimi.ingsw.network.netMessage.c2s;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

/**
 * Class LobbyJoinedMessage
 * used to request the server to join a lobby
 */
public class LobbyJoinedMessage extends NetMessage {
    private final Player player;
    private final Integer ID;

    /**
     * Class constructor
     * @param player the player
     * @param ID the lobby to join
     */
    public LobbyJoinedMessage(Player player,Integer ID) {
        this.player = player;
        this.ID=ID;
    }

    /**
     * @return Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return Integer
     */
    public Integer getID() {
        return ID;
    }
}
