package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.deck.DeckTypeBox;
import it.polimi.ingsw.network.netMessage.NetMessage;

/**
 * Class CardDrawnFromSourceMessage
 * used to inform the views that a card has been drawn from a deck/deckBuffer
 */
public class CardDrawnFromSourceMessage extends NetMessage {
    private final Integer ID;
    private final DeckTypeBox type;
    private final Card card;

    /**
     * Class constructor
     * @param ID the game ID
     * @param type the deck/deckbuffer used
     * @param card the new card on top of the deck
     */
    public CardDrawnFromSourceMessage(Integer ID, DeckTypeBox type, Card card) {
        this.ID = ID;
        this.type = type;
        this.card = card;
    }

    /**
     * @return Integer
     */
    public Integer getID() {
        return ID;
    }

    /**
     * @return DeckTypeBox
     */
    public DeckTypeBox getType() {
        return type;
    }

    /**
     * @return Card
     */
    public Card getCard() {
        return card;
    }
}
