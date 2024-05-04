package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

import java.io.Serializable;

public class LobbyCreatedMessage extends NetMessage implements Serializable  {
    private final Player creator;
    private final Lobby lobby;
    private final Integer ID;

    public LobbyCreatedMessage(Player creator, Lobby lobby,Integer ID) {
        this.creator = creator;
        this.lobby = lobby;
        this.ID=ID;
    }

    public Player getCreator() {
        return creator;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public Integer getID() {
        return ID;
    }
}
