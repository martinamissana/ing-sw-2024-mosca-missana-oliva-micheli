package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

/**
 * Class LobbyLeftMessage
 * used to inform the views that someone left the lobby
 */
public class LobbyLeftMessage extends NetMessage {
    private final Player player;
    private final Lobby lobby;
    private final Integer ID;

    /**
     * Class constructor
     * @param player the player who left the lobby
     * @param lobby  the lobby left
     * @param ID     the lobby ID
     */
    public LobbyLeftMessage(Player player, Lobby lobby,Integer ID) {
        this.player = player;
        this.lobby = lobby;
        this.ID=ID;
    }

    /**
     * @return Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return Lobby
     */
    public Lobby getLobby() {
        return lobby;
    }

    /**
     * @return Integer
     */
    public Integer getID() {
        return ID;
    }
}
