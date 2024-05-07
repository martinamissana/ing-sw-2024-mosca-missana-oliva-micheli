package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

public class FailMessage extends NetMessage {
    private final String message;
    private final Player player;
    public FailMessage(String message,Player player) {
        this.player=player;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Player getPlayer() {
        return player;
    }
}
