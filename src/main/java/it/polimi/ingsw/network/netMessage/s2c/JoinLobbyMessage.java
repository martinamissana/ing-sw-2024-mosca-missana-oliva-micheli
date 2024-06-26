package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

/**
 * Class JoinLobbyMessage
 * used to inform the views that someone has joined a lobby
 */
public class JoinLobbyMessage extends NetMessage {
    private final Player player;
    private final Integer ID;

    /**
     * Class constructor
     * @param player the player who joined the lobby
     * @param ID     the lobby joined
     */
    public JoinLobbyMessage(Player player,Integer ID) {
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
