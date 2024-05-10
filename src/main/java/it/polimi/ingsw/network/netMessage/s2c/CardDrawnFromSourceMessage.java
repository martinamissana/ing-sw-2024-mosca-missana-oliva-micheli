package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.deck.DeckTypeBox;
import it.polimi.ingsw.network.netMessage.NetMessage;

public class CardDrawnFromSourceMessage extends NetMessage {
    private final Integer ID;
    private final DeckTypeBox type;
    private final Card card; //the new card on top

    public CardDrawnFromSourceMessage(Integer ID, DeckTypeBox type, Card card) {
        this.ID = ID;
        this.type = type;
        this.card = card;
    }

    public Integer getID() {
        return ID;
    }

    public DeckTypeBox getType() {
        return type;
    }

    public Card getCard() {
        return card;
    }
}
