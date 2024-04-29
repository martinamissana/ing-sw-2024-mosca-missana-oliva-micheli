package it.polimi.ingsw.network.netMessage.c2s;

import it.polimi.ingsw.network.netMessage.NetMessage;

public class MyNickname extends NetMessage {
    String nickname;

    public MyNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
