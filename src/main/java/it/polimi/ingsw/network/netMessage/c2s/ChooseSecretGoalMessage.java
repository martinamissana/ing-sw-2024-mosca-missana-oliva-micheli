package it.polimi.ingsw.network.netMessage.c2s;

import it.polimi.ingsw.network.netMessage.NetMessage;

public class ChooseSecretGoalMessage extends NetMessage {
    private final Integer ID;
    private final String nickname;
    private final int goalID;

    public ChooseSecretGoalMessage(Integer ID, String nickname, int goalID) {
        this.ID = ID;
        this.nickname = nickname;
        this.goalID = goalID;
    }

    public Integer getID() {
        return ID;
    }

    public String getNickname() {
        return nickname;
    }

    public int getGoalID() {
        return goalID;
    }
}
