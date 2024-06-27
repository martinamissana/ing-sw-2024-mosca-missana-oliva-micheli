package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.exceptions.HandIsFullException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Hand Class<br>
 * Each player has an associated {@code Hand} containing all their available cards
 */
public class Hand implements Serializable {
    private final ArrayList<Card> handList = new ArrayList<>();

    /**
     * Returns number of cards in hand
     * @return size
     */
    public int getSize() { return handList.size(); }

    /**
     * Returns the card at the specified position in the hand
     * @param pos position of the desired card
     * @return Card
     * @throws IndexOutOfBoundsException thrown if the position requested isn't in the list
     */
    public Card getCard(int pos) throws IndexOutOfBoundsException { return handList.get(pos); }

    /**
     * Adds a card to the hand
     * @param card card to add to the hand
     * @throws HandIsFullException thrown if the hand already contains three (or more) cards
     */
    public void addCard(Card card) throws HandIsFullException {
        if (this.handList.size() > 2)
            throw new HandIsFullException();
        this.handList.add(card);
    }

    /**
     * Removes a card from the hand
     * @param card card to remove from the hand
     */
    public void removeCard(Card card) {
        if (!this.handList.remove(card))
            throw new NoSuchElementException();
    }

    /**
     * <strong>For testing purposes only</strong><br>
     * Removes all cards from the hand
     */
    public void removeAllCards() { handList.clear(); }
}