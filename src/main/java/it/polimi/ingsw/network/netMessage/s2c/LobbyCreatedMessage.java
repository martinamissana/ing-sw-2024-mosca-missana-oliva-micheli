package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

import java.io.Serializable;

/**
 * Class LobbyCreatedMessage
 * used to inform the views that a lobby has been created
 */
public class LobbyCreatedMessage extends NetMessage implements Serializable  {
    private final Player creator;
    private final Lobby lobby;
    private final Integer ID;

    /**
     * Class constructor
     * @param creator player who created the lobby
     * @param lobby   the created lobby
     * @param ID      the lobby ID
     */
    public LobbyCreatedMessage(Player creator, Lobby lobby,Integer ID) {
        this.creator = creator;
        this.lobby = lobby;
        this.ID=ID;
    }

    /**
     * @return Player
     */
    public Player getCreator() {
        return creator;
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
