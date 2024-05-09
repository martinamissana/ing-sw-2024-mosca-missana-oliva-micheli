package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.network.netMessage.NetMessage;

public class CardPlacedOnFieldMessage extends NetMessage {
    private final Coords coords;
    private final Integer ID;
    private final Card card;
    private final String nickname;

    public CardPlacedOnFieldMessage(Coords coords, Integer ID, Card card, String nickname) {
        this.coords = coords;
        this.ID = ID;
        this.card = card;
        this.nickname = nickname;
    }

    public Coords getCoords() {
        return coords;
    }
    public Integer getID() {
        return ID;
    }

    public String getNickname() {
        return nickname;
    }

    public Card getCard() {
        return card;
    }
}
