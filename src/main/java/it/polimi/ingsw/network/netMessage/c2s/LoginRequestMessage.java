package it.polimi.ingsw.network.netMessage.c2s;

import it.polimi.ingsw.network.netMessage.NetMessage;

/**
 * Class LoginRequestMessage
 * used to request the server to log in
 */
public class LoginRequestMessage extends NetMessage {
    String nickname;

    /**
     * Class constructor
     * @param nickname name chosen to log in
     */
    public LoginRequestMessage(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return String
     */
    public String getNickname() {
        return nickname;
    }
}
