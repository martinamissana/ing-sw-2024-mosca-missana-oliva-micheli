package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.exceptions.EmptyBufferException;
import it.polimi.ingsw.model.exceptions.EmptyDeckException;

import java.io.Serializable;

/**
 * Visible drawable card on the board
 */
public class DeckBuffer implements Drawable, Serializable {
    private ResourceCard card;
    private final Deck deck;

    /**
     * Class constructor
     *
     * @param deck from which the deck buffer draws
     */
    public DeckBuffer(Deck deck) {
        this.deck = deck;
        this.card = null;
    }

    /**
     * Gets the card in the deck buffer
     *
     * @return card
     */
    public ResourceCard getCard() { return card; }

    public void setCard(ResourceCard card) { this.card = card; }

    /**
     * Put a card in the deck buffer (if empty)
     */
    public void refill() {
        if (!deck.isEmpty() && card == null)
            try {
                this.card = this.deck.draw();
                if (this.card.getSide().equals(CardSide.BACK)) card.flip();
            } catch (EmptyDeckException ignored) {
            }
    }

    @Override
    public ResourceCard draw() throws EmptyBufferException {
        if (card != null) {
            ResourceCard drawn = card;
            card = null;
            this.refill();
            return drawn;
        } else throw new EmptyBufferException();
    }
}
