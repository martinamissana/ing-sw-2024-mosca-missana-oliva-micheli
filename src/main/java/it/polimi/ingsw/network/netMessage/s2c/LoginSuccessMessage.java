package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.network.netMessage.NetMessage;

import java.io.Serializable;

/**
 * Class LoginSuccessMessage
 * used to inform the view that the login has been successful
 */
public class LoginSuccessMessage extends NetMessage implements Serializable {
    String nickname;

    /**
     * Class constructor
     * @param nickname name of who logged in
     */
    public LoginSuccessMessage(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return String
     */
    public String getNickname() {
        return nickname;
    }
}
