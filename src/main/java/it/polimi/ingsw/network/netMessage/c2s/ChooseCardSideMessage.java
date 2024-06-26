package it.polimi.ingsw.network.netMessage.c2s;

import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.network.netMessage.NetMessage;

/**
 * Class ChooseCardSideMessage
 * used to choose the side of the starter card
 */
public class ChooseCardSideMessage extends NetMessage {
    private final Integer ID;
    private final String nickname;
    private final CardSide side;

    /**
     * Class constructor
     * @param ID the game ID
     * @param nickname the name of the player
     * @param side the side chosen
     */
    public ChooseCardSideMessage(Integer ID, String nickname, CardSide side) {
        this.ID = ID;
        this.nickname = nickname;
        this.side = side;
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
     * @return CardSide
     */
    public CardSide getSide() {
        return side;
    }
}
