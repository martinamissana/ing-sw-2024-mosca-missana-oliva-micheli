package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.game.*;
import it.polimi.ingsw.model.exceptions.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Class Deck<br>
 * Each game instantiates a resource deck and a golden deck.<br>
 * Cards are stored in reverse drawing order, meaning that the first one to leave the deck is the last in the {@code cards} list.
 */
public class Deck implements Drawable, Serializable {
    private final DeckType type;
    private final ArrayList<ResourceCard> cards;

    /**
     * Class constructor
     * @param type type of cards in the deck ({@code RESOURCE} or {@code GOLDEN})
     * @throws IOException thrown when an error occurs in reading the json file
     */
    public Deck(DeckType type) throws IOException {
        this.type = type;
        this.cards = new ArrayList<>();

        if (type == DeckType.RESOURCE)
            this.cards.addAll(CardsPreset.getResourceCards());
        else
            this.cards.addAll(CardsPreset.getGoldenCards());
    }

    /**
     * @return the type of cards in the deck
     */
    public DeckType getType() { return this.type; }

    /**
     * @return the last card in the deck, which is the first one to be drawn from the deck
     */
    public ResourceCard getLast() { return this.cards.getLast(); }


    /**
     * @return {@code true} if the deck is empty, {@code false} if there's at least one card
     */
    public boolean isEmpty() { return this.cards.isEmpty(); }

    /**
     * Shuffles the deck
     */
    public void shuffle() { Collections.shuffle(cards); }

    @Override
    public ResourceCard draw() throws EmptyDeckException {
        if (!cards.isEmpty()) return cards.removeLast();
        else throw new EmptyDeckException();
    }
}
