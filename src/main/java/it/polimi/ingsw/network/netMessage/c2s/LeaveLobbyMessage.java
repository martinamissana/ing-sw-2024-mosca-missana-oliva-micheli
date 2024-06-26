package it.polimi.ingsw.network.netMessage.c2s;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

/**
 * Class LeaveLobbyMessage
 * used to request the server to leave a lobby
 */
public class LeaveLobbyMessage extends NetMessage {
    private final Player player;
    private final Integer ID;

    /**
     * Class constructor
     * @param player the player
     * @param ID the lobby to leave
     */
    public LeaveLobbyMessage(Player player,Integer ID) {
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
