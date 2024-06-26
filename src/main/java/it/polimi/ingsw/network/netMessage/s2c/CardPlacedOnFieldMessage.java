package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.network.netMessage.NetMessage;

/**
 * Class CardPlacedOnFieldMessage
 * used to inform the views that a card has been placed in a field
 */
public class CardPlacedOnFieldMessage extends NetMessage {
    private final Coords coords;
    private final Integer ID;
    private final Card card;
    private final CardSide side;
    private final String nickname;

    /**
     * Class constructor
     * @param coords the coordinates where the card has been placed
     * @param ID the game ID
     * @param card the card placed on field
     * @param side the side of the card placed
     * @param nickname the name of the player who placed the card
     */
    public CardPlacedOnFieldMessage(Coords coords, Integer ID, Card card, CardSide side, String nickname) {
        this.coords = coords;
        this.ID = ID;
        this.card = card;
        this.side = side;
        this.card.setSide(card.getSide());
        this.nickname = nickname;
    }

    /**
     * @return Coords
     */
    public Coords getCoords() {
        return coords;
    }

    /**
     * @return Integer
     */
    public Integer getID() {
        return ID;
    }

    /**
     * @return String
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @return Card
     */
    public Card getCard() {
        return card;
    }

    /**
     * @return CardSide
     */
    public CardSide getSide() {
        return side;
    }
}
