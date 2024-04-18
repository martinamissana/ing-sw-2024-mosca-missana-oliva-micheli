package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.model.card.*;
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
     * @param deck from which the deck buffer draws
     */
    public DeckBuffer(Deck deck) {
        this.deck = deck;
    }

    /**
     * @return card
     */
    public ResourceCard getCard() {
        return card;
    }

    /**
     * Put a card in the deck buffer (if empty)
     */
    public void refill() throws EmptyDeckException{
        if(!deck.getDeck().isEmpty()) {
            card = deck.draw();
        } else throw new EmptyDeckException("Deck is empty. Cannot draw!");
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
    public ResourceCard draw() {
        ResourceCard drawn = card;
        card = null;
        try {
            this.refill();
        }
        catch(EmptyDeckException e) {
            System.out.println("Cannot refill the space");
        }

        return drawn;
    }
}
