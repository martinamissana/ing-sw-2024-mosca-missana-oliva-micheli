package it.polimi.ingsw.model.player;

import java.io.Serializable;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.exceptions.HandIsFullException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Hand Class
 * each player has an associated hand containing all their available cards
 */
public class Hand implements Serializable {
    private final ArrayList<Card> handList = new ArrayList<>();

    /**
     * returns number of cards in hand
     * @return size
     */
    public int getSize() {
        return handList.size();
    }

    /**
     * returns the card at the specified position in the hand
     * @param pos position of the desired card
     * @return Card
     * @throws IndexOutOfBoundsException thrown if the position requested isn't in the list
     */
    public Card getCard(int pos) throws IndexOutOfBoundsException { return handList.get(pos); }

    /**
     * adds a card to the hand
     * @param card card to add to the hand
     * @throws HandIsFullException thrown if the hand already contains three (or more) cards
     */
    public void addCard(Card card) throws HandIsFullException {
        if (this.handList.size() > 2)
            throw new HandIsFullException();
        this.handList.add(card);
    }

    /**
     * removes a card from the hand
     * returns false if the specified card wasn't in the hand
     * @param card card to remove from the hand
     */
    public void removeCard(Card card) {
        if (!this.handList.remove(card))
            throw new NoSuchElementException();
    }
}