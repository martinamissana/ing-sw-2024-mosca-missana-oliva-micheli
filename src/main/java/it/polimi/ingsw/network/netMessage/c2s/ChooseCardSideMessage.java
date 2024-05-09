package it.polimi.ingsw.network.netMessage.c2s;

import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.network.netMessage.NetMessage;

public class ChooseCardSideMessage extends NetMessage {
    private final Integer ID;
    private final String nickname;
    private final CardSide side;

    public ChooseCardSideMessage(Integer ID, String nickname, CardSide side) {
        this.ID = ID;
        this.nickname = nickname;
        this.side = side;
    }

    public Integer getID() {
        return ID;
    }

    public String getNickname() {
        return nickname;
    }

    public CardSide getSide() {
        return side;
    }
}
