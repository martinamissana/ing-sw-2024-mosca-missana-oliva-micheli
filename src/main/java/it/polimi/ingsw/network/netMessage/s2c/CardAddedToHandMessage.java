package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

/**
 * Class CardAddedToHandMessage
 * used to inform the view that a card has been added to the hand
 */
public class CardAddedToHandMessage extends NetMessage {
    private final Player player;
    private final Card card;

    /**
     * Class constructor
     * @param player the player
     * @param card  the card added to the hand
     */
    public CardAddedToHandMessage(Player player, Card card) {
        this.player = player;
        this.card = card;
    }

    /**
     * @return Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return Card
     */
    public Card getCard() {
        return card;
    }
}
