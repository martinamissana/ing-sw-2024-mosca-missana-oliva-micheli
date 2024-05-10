package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.network.netMessage.NetMessage;

public class LastRoundStartedMessage extends NetMessage {
    private final Integer ID;

    public LastRoundStartedMessage(Integer ID) {
        this.ID = ID;
    }

    public Integer getID() {
        return ID;
    }
}
