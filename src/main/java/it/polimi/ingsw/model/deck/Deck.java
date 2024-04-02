package it.polimi.ingsw.model.deck;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.game.CardsPreset;

import java.io.IOException;
import java.util.*;


public class Deck implements Drawable {
    private int numCards;  // is needed?? (cards.size())
    private DeckType type;
    private ArrayList<Card> cards;

    public Deck(DeckType type) throws IOException{
        this.numCards = 40;
        this.cards = new ArrayList<>(40);
        this.type = type;
        if (type == DeckType.RESOURCE) CardsPreset.getResourceCards();
        else CardsPreset.getGoldenCards();
    }

    public ArrayList<Card> getDeck() {
        return this.cards;
    }
    public DeckType getType() {
        return this.type;
    }
    public int getNumCards() {return numCards;}
    public void setType(DeckType type) {
        this.type = type;
        return;
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public boolean isEmpty() {
       return numCards == 0;
    }

    @Override
    public Card draw() {
        Card drawn = this.cards.get(this.numCards - 1);
        cards.remove(drawn);
        numCards--;

        return drawn;
    }

    public boolean equals(ArrayList<Card> resource) {
        for(int i = 0; i < resource.size(); i++) {
            if(resource.get(i) != this.getDeck().get(i)) {
                return false;
            }
        }
        return true;
    }
}
