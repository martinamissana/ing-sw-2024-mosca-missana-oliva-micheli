package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

public class LobbyLeftMessage extends NetMessage {
    private final Player player;
    private final Lobby lobby;
    private final Integer ID;

    public LobbyLeftMessage(Player player, Lobby lobby,Integer ID) {
        this.player = player;
        this.lobby = lobby;
        this.ID=ID;
    }

    public Player getPlayer() {
        return player;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public Integer getID() {
        return ID;
    }
}
