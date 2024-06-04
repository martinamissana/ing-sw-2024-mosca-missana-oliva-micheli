package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.player.Player;

/**
 *  CardRemovedFromHandEvent class
 *  Extends abstract class Event
 *  Used to notify the virtual view of the client when a card is removed from the hand
 */
public class CardRemovedFromHandEvent  extends Event{
    private final Player player;
    private final Card card;

    /**
     * class constructor
     * @param player player whose hand the card is removed from
     * @param card card removed
     */
    public CardRemovedFromHandEvent(Player player, Card card) {
        this.player = player;
        this.card = card;
    }

    /**
     * getter
     * @return player whose hand the card is removed from
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * getter
     * @return card removed from the hand
     */
    public Card getCard() {
        return card;
    }
}
