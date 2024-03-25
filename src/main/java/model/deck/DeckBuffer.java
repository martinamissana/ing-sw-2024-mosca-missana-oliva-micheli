package model.deck;
import model.card.Card;
import model.card.GoldenCard;
import model.card.ResourceCard;

public class DeckBuffer implements Drawable {
    private Card card;
    private Deck deck;

    public DeckBuffer(Deck deck) {
        if (deck.getType().equals(DeckType.RESOURCE)) {
            card = new ResourceCard();
        }
        else if (deck.getType().equals(DeckType.GOLDEN)) {
            card = new GoldenCard();
        }
        else return;

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
