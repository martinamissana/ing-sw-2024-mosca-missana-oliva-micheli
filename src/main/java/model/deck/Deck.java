package model.deck;
import model.card.*;
import java.util.*;

public class Deck implements Drawable {
    private int num_cards;  // is needed?? (cards.size())
    private DeckType type;
    private ArrayList<Card> cards;

    public Deck() {
        this.num_cards = 40;
        this.cards = new ArrayList<>(num_cards);
    }
    public void ResourceDeck() {
        this.type = DeckType.RESOURCE;
        // Collection cards queried
    }

    public void GoldenDeck() {
        this.type = DeckType.GOLDEN;
        // Collection cards queried
    }

    public ArrayList<Card> getDeck() {
        return this.cards;
    }
    public DeckType getType() {
        return this.type;
    }

    private void shuffle() {
        Collections.shuffle(cards);
    }

    public boolean isEmpty() {
       return num_cards == 0;
    }

    @Override
    public Card draw() {
        Card drawn = null;
        if(this.type == DeckType.RESOURCE) {
            drawn = new ResourceCard();
        }
        else if(this.type == DeckType.GOLDEN) {
            drawn = new GoldenCard();
        }
        drawn = this.cards.get(this.num_cards - 1);
        cards.remove(drawn);
        num_cards--;

        return drawn;
    }
}
