package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.player.Player;

/**
 * CardAddedToHandEvent class<br>
 * Extends abstract class Event<br>
 * Used to notify the virtual view of the client when a card is added to the player hand
 */
public class CardAddedToHandEvent extends Event {
    private final Player player;
    private final Card card;
    private final Integer ID;

    /**
     * Class constructor
     * @param player who's hand the card is added to
     * @param card card added to the hand
     */
    public CardAddedToHandEvent(Player player, Card card, Integer id) {
        this.player = player;
        this.card = card;
        ID = id;
    }

    /**
     * @return Player whose hand the card is added to
     */
    public Player getPlayer() { return player; }

    /**
     * @return Card added to hand
     */
    public Card getCard() { return card; }

    /**
     * @return game ID
     */
    public Integer getID() { return ID; }
}
