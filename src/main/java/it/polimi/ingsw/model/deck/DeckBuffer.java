package it.polimi.ingsw.model.deck;
import it.polimi.ingsw.model.card.Card;

import java.io.Serializable;

/**
 * Visible drawable card on the board
 */
public class DeckBuffer implements Drawable, Serializable {
    private Card card;
    private final Deck deck;

    /**
     * Class constructor
     * @param deck from which the deck buffer draws
     */
    public DeckBuffer(Deck deck) {
        this.deck = deck;
    }

    /**
     * Put a card in the deck buffer (if empty)
     */
    private void refill() {
        if(!deck.getDeck().isEmpty()) {
            card = deck.draw();
        }
    }

    /**
     * @return type of deck and deck buffer
     */
    public DeckType getType() {
        return this.deck.getType();
    }

    /**
     * Draws the card on the deck buffer
     * @return drawn
     */
    @Override
    public Card draw() {
        Card drawn = card;
        card = null;
        this.refill();

        return drawn;
    }
}
