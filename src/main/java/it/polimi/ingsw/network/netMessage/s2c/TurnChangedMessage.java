package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.network.netMessage.NetMessage;

public class TurnChangedMessage extends NetMessage {
    private final Integer ID;
    private final String nickname; //nickname of the player of the current turn

    public TurnChangedMessage(Integer ID, String nickname) {
        this.ID = ID;
        this.nickname = nickname;
    }

    public Integer getID() {
        return ID;
    }

    public String getNickname() {
        return nickname;
    }
}
