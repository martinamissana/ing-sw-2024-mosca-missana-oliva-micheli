package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.network.netMessage.NetMessage;

public class LobbyDeletedMessage extends NetMessage {
    private Integer ID;

    public LobbyDeletedMessage(Integer ID) {
        this.ID = ID;
    }

    public Integer getID() {
        return ID;
    }
}
