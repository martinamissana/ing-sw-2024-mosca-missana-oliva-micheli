package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.player.Player;

/**
 * CardAddedToHandEvent class
 * Extends abstract class Event
 * Used to notify the virtual view of the client when a card is added to the player hand
 */
public class CardAddedToHandEvent extends Event {
    private final Player player;
    private final Card card;

    /**
     * Class constructor
     * @param player who's hand the card is added to
     * @param card card added to the hand
     */
    public CardAddedToHandEvent(Player player, Card card) {
        this.player = player;
        this.card = card;
    }

    /**
     * getter
     * @return Player whose hand the card is added to
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * getter
     * @return Card added to hand
     */
    public Card getCard() {
        return card;
    }
}
