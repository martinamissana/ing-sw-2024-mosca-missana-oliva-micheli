package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.network.netMessage.NetMessage;

public class GameTerminatedMessage extends NetMessage {
    private final Integer ID;

    public GameTerminatedMessage(Integer ID) {
        this.ID = ID;
    }

    public Integer getID() {
        return ID;
    }
}
