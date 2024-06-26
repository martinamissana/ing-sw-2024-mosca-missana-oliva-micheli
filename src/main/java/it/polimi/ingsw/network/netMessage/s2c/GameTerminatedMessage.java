package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.network.netMessage.NetMessage;

/**
 * Class GameTerminatedMessage
 * used to inform the views that the game is finished
 */
public class GameTerminatedMessage extends NetMessage {
    private final Integer ID;

    /**
     * Class constructor
     * @param ID the game ID
     */
    public GameTerminatedMessage(Integer ID) {
        this.ID = ID;
    }

    /**
     * @return Integer
     */
    public Integer getID() {
        return ID;
    }
}
