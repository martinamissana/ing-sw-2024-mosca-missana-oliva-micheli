package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.game.Action;
import it.polimi.ingsw.network.netMessage.NetMessage;

public class GameActionSwitchedMessage extends NetMessage {
    private final Integer ID;
    private final Action action;

    public GameActionSwitchedMessage(Integer ID, Action action) {
        this.ID = ID;
        this.action = action;
    }

    public Integer getID() {
        return ID;
    }

    public Action getAction() {
        return action;
    }
}
