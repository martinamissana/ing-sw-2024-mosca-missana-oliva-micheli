package it.polimi.ingsw.network.netMessage.c2s;

import it.polimi.ingsw.network.netMessage.NetMessage;

public class FlipCardMessage extends NetMessage {
    private final Integer gameID;
    private final String nickname;
    private final int handPos;

    public FlipCardMessage(Integer gameID, String nickname, int handPos) {
        this.gameID = gameID;
        this.nickname = nickname;
        this.handPos = handPos;
    }

    public Integer getGameID() {
        return gameID;
    }

    public String getNickname() {
        return nickname;
    }

    public int getHandPos() {
        return handPos;
    }
}
