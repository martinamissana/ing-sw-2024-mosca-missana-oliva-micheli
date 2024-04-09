package it.polimi.ingsw.model.deck;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.game.CardsPreset;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;


/**
 * List of cards of type RESOURCE or GOLDEN
 */
public class Deck implements Drawable, Serializable {
    private final DeckType type;
    private final ArrayList<ResourceCard> cards;

    /**
     * Class constructor
     * @param type of the deck (RESOURCE / GOLDEN)
     * @throws IOException (for adding cards in the deck)
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
     * @return last card on cards ArrayList
     */
    @Override
    public ResourceCard draw() {
        if(!cards.isEmpty()) return cards.removeLast();
        else return null;
    }
}
