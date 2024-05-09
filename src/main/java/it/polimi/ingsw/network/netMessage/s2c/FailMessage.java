package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.network.netMessage.NetMessage;

public class FailMessage extends NetMessage {
    private final String message;
    private final String string;
    public FailMessage(String message, String string) {
        this.string = string;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getString() {
        return string;
    }
}
