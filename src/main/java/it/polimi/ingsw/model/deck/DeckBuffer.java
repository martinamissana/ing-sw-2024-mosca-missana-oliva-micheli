package it.polimi.ingsw.model.deck;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.GoldenCard;
import it.polimi.ingsw.model.card.ResourceCard;

public class DeckBuffer implements Drawable {
    private Card card;
    private Deck deck;

    public DeckBuffer(Deck deck) {
        this.deck = deck;
        if(!deck.isEmpty()) this.refill();
    }
    
    private void refill() {
        if(!deck.isEmpty()) {
            card = deck.draw();
        }
    }

    public DeckType getType() {
        return this.deck.getType();
    }

    @Override
    public Card draw() {
        Card drawn = card;
        card = null;
        this.refill();

        return drawn;
    }
}
