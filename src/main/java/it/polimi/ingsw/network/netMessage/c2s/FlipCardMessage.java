package it.polimi.ingsw.network.netMessage.c2s;

import it.polimi.ingsw.network.netMessage.NetMessage;

/**
 * Class FlipCardMessage
 * used to request the server to flip a card
 */
public class FlipCardMessage extends NetMessage {
    private final Integer gameID;
    private final String nickname;
    private final int handPos;

    /**
     * Class constructor
     * @param gameID the game ID
     * @param nickname the name of the player
     * @param handPos the position in the hand of the card
     */
    public FlipCardMessage(Integer gameID, String nickname, int handPos) {
        this.gameID = gameID;
        this.nickname = nickname;
        this.handPos = handPos;
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
}
