package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

public class JoinLobbyMessage extends NetMessage {
    private final Player player;
    private final Integer ID;

    public JoinLobbyMessage(Player player,Integer ID) {
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
