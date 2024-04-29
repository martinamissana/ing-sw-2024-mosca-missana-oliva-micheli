package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.network.netMessage.NetMessage;

public class LoginMessage extends NetMessage {
    String nickname;

    public LoginMessage(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
