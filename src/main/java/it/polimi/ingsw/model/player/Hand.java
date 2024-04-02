package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.Card;
import java.util.ArrayList;

/**
 * Hand Class
 * each player has an associated hand containing all their available cards
 */
public class Hand {
    private final ArrayList<Card> handList;

    /**
     * Class constructor
     */
    public Hand() {
        this.handList = new ArrayList<Card>();
    }

    /**
     * returns the card at the specified position in the hand
     * @param pos
     * @return Card
     */
    public Card getCard(int pos) {
        return handList.get(pos);
    }

    /**
     * adds a card to the hand, returning true.
     * returns false if the hand is full
     * @param card
     * @return boolean
     */
    public boolean addCard(Card card) {
        if (this.handList.size() > 2) // does it need to check card == null?
            return false;
        return this.handList.add(card);
    }

    /**
     * removes a card from the hand, returning true.
     * returns false if the specified card wasn't in the hand
     * @param card
     * @return boolean
     */
    public boolean removeCard(Card card) {
        return this.handList.remove(card);
    }
}