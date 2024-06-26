package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.network.netMessage.NetMessage;

/**
 * Class FailMessage
 * used to warn the view when they try to perform a wrong move
 */
public class FailMessage extends NetMessage {
    private final String message;
    private final String nickname;

    /**
     * Class constructor
     * @param message the warning message
     * @param nickname the nickname of the player who tried the move
     */
    public FailMessage(String message, String nickname) {
        this.nickname = nickname;
        this.message = message;
    }


    /**
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return String
     */
    public String getNickname() {
        return nickname;
    }
}
