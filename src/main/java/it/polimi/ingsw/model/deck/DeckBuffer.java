package it.polimi.ingsw.model.deck;
import it.polimi.ingsw.model.card.Card;

/**
 * Visible drawable card on the board
 */
public class DeckBuffer implements Drawable {
    private Card card;
    private Deck deck;

    /**
     * Class constructor
     * @param deck
     */
    public DeckBuffer(Deck deck) {
        this.deck = deck;
        if(!deck.getDeck().isEmpty()) this.refill();
    }

    /**
     * Put a card in the deck buffer
     */
    private void refill() {
        if(!deck.getDeck().isEmpty()) {
            card = deck.draw();
        }
    }

    /**
     * @return DeckType
     */
    public DeckType getType() {
        return this.deck.getType();
    }

    /**
     * Draws the card on the deck buffer
     */
    @Override
    public Card draw() {
        Card drawn = card;
        card = null;
        this.refill();

        return drawn;
    }
}
