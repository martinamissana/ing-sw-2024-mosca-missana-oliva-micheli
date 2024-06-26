package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.network.netMessage.NetMessage;

/**
 * Class LobbyDeletedMessage
 * used to inform the views that a lobby has been deleted
 */
public class LobbyDeletedMessage extends NetMessage {
    private Integer ID;

    /**
     * Class constructor
     * @param ID the lobby ID
     */
    public LobbyDeletedMessage(Integer ID) {
        this.ID = ID;
    }

    /**
     * @return Integer
     */
    public Integer getID() {
        return ID;
    }
}
