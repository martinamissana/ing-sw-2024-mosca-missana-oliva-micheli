package it.polimi.ingsw.network.netMessage.c2s;

import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.network.netMessage.NetMessage;

public class PlayCardMessage extends NetMessage {
    private final Integer gameID;
    private final String nickname;
    private final int handPos;
    private final Coords coords;
    private final CardSide side;

    public PlayCardMessage(Integer gameID, String nickname, int handPos, Coords coords, CardSide side) {
        this.gameID = gameID;
        this.nickname = nickname;
        this.handPos = handPos;
        this.coords = coords;
        this.side = side;
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

    public Coords getCoords() {
        return coords;
    }

    public CardSide getSide() {
        return side;
    }
}
