package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

public class CardAddedToHandMessage extends NetMessage {
    private final Player player;
    private final Card card;

    public CardAddedToHandMessage(Player player, Card card) {
        this.player = player;
        this.card = card;
    }

    public Player getPlayer() {
        return player;
    }
    public Card getCard() {
        return card;
    }
}
