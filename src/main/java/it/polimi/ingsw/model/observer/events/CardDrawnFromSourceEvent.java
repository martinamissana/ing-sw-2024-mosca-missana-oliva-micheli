package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.deck.DeckTypeBox;

/**
 *  CardDrawnFormSourceEvent class
 *  Extends abstract class Event
 *  Used to notify the virtual views of the clients in a game when a card is drawn from decks or deckbuffers
 */
public class CardDrawnFromSourceEvent  extends Event{
    private final Integer ID;
    private final DeckTypeBox type;
    private final Card card; //the new card on top

    /**
     * Class constructor
     * @param ID ID of the game
     * @param type source from where the card is drawn
     * @param card the new card on top of the ResDeck [for ResDeck, Res1, Res2] or GoldDeck [for GoldDeck, Gold1, Gold2]
     */
    public CardDrawnFromSourceEvent(Integer ID, DeckTypeBox type, Card card) {
        this.ID = ID;
        this.type = type;
        this.card = card;
    }

    /**
     * getter
     * @return ID of the game
     */
    public Integer getID() {
        return ID;
    }

    /**
     * getter
     * @return the source where te card is drawn
     */
    public DeckTypeBox getType() {
        return type;
    }

    /**
     * getter
     * @return the new card on top of the ResDeck [for ResDeck, Res1, Res2] or GoldDeck [for GoldDeck, Gold1, Gold2]
     */
    public Card getCard() {
        return card;
    }
}
