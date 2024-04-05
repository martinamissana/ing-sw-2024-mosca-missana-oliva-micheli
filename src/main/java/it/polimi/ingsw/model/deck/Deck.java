package it.polimi.ingsw.model.deck;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.game.CardsPreset;

import java.io.IOException;
import java.util.*;


/**
 * List of cards of type RESOURCE or GOLDEN
 */
public class Deck implements Drawable {
    private final DeckType type;
    private ArrayList<ResourceCard> cards;

    /**
     * Class constructor
     * @param type
     * @throws IOException
     */
    public Deck(DeckType type) throws IOException {
        this.type = type;
        this.cards = new ArrayList<>();

        if (type == DeckType.RESOURCE) {
            this.cards.addAll(CardsPreset.getResourceCards());
        }
        else {
            this.cards.addAll(CardsPreset.getGoldenCards());
        }
    }

    /**
     * gets the list of cards in the deck
     * @return cards
     */
    public ArrayList<ResourceCard> getDeck() {
        return this.cards;
    }

    /**
     * gets the type of the deck
     * @return type
     */
    public DeckType getType() {
        return this.type;
    }

    /**
     * Shuffles the deck
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Draws the last card from the deck list
     */
    @Override
    public ResourceCard draw() {
        if(!cards.isEmpty()) return cards.removeLast();
        else return null;
    }
}
