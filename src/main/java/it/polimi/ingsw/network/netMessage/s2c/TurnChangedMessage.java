package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.network.netMessage.NetMessage;

/**
 * Class TurnChangedMessage
 * used to inform the views that the turn has changed
 */
public class TurnChangedMessage extends NetMessage {
    private final Integer ID;
    private final String nickname;
    private final boolean lastRound;

    /**
     * Class constructor
     * @param ID        the game ID
     * @param nickname  nickname of the player of the current turn
     * @param lastRound boolean true if it's the last round
     */
    public TurnChangedMessage(Integer ID, String nickname, boolean lastRound) {
        this.ID = ID;
        this.nickname = nickname;
        this.lastRound = lastRound;
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
     * @return boolean
     */
    public boolean isLastRound() {
        return lastRound;
    }
}
