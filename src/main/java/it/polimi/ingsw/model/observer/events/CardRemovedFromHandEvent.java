package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.player.Player;

/**
 *  CardRemovedFromHandEvent class<br>
 *  Extends abstract class Event<br>
 *  Used to notify the virtual view of the client when a card is removed from the hand
 */
public class CardRemovedFromHandEvent  extends Event{
    private final Player player;
    private final Card card;

    /**
     * Class constructor
     * @param player player whose hand the card is removed from
     * @param card card removed
     */
    public CardRemovedFromHandEvent(Player player, Card card) {
        this.player = player;
        this.card = card;
    }

    /**
     * @return player whose hand the card is removed from
     */
    public Player getPlayer() { return player; }

    /**
     * @return card removed from the hand
     */
    public Card getCard() { return card; }
}
