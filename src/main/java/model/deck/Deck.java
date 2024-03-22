package model.deck;
import model.card.Card;

import java.util.Set;

public class Deck implements Drawable {
    private int num_cards;  // is needed?? (cards.size())
    private DeckType type;
    private Set<Card> cards;

    public void ResourceDeck() {
        this.num_cards = 40;
        this.type = DeckType.RESOURCE;
        // Collection cards queried
    }

    public void GoldenDeck() {
        this.num_cards = 40;
        this.type = DeckType.GOLDEN;
        // Collection cards queried
    }

    public DeckType getType() {
        return this.type;
    }

    private void shuffle() {
    }

    public boolean isEmpty() {
        return num_cards == 0;
    }

    @Override
    public Card draw() {
        return null;
    }
}
