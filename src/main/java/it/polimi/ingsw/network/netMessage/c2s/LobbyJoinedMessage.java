package it.polimi.ingsw.network.netMessage.c2s;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

public class LobbyJoinedMessage extends NetMessage {
    private final Player player;
    private final Integer ID;

    public LobbyJoinedMessage(Player player,Integer ID) {
        this.player = player;
        this.ID=ID;
    }

    public Player getPlayer() {
        return player;
    }
    public Integer getID() {
        return ID;
    }
}
