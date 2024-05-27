package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.network.netMessage.NetMessage;

public class FailMessage extends NetMessage {
    private final String message;
    private final String nickname;
    public FailMessage(String message, String string) {
        this.nickname = string;
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    public String getNickname() {
        return nickname;
    }
}
