package it.polimi.ingsw.network.netMessage.c2s;

import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.network.netMessage.NetMessage;

/**
 * Class PlayCardMessage
 * used to request the server to play a card on the field
 */
public class PlayCardMessage extends NetMessage {
    private final Integer gameID;
    private final String nickname;
    private final int handPos;
    private final Coords coords;
    private final CardSide side;

    /**
     * Class constructor
     * @param gameID the game ID
     * @param nickname the name of the player
     * @param handPos the position of the card in the player's hand
     * @param coords the coordinates where to put the card
     * @param side the side of the card
     */
    public PlayCardMessage(Integer gameID, String nickname, int handPos, Coords coords, CardSide side) {
        this.gameID = gameID;
        this.nickname = nickname;
        this.handPos = handPos;
        this.coords = coords;
        this.side = side;
    }

    /**
     * @return Integer
     */
    public Integer getGameID() {
        return gameID;
    }

    /**
     * @return String
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @return int
     */
    public int getHandPos() {
        return handPos;
    }

    /**
     * @return Coords
     */
    public Coords getCoords() {
        return coords;
    }

    /**
     * @return CardSide
     */
    public CardSide getSide() {
        return side;
    }
}
