package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.network.netMessage.NetMessage;

public class FailMessage extends NetMessage {
    private final String message;

    public FailMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
