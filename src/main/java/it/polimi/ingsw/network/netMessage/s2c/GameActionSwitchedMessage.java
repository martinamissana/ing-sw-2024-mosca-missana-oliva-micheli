package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.game.Action;
import it.polimi.ingsw.network.netMessage.NetMessage;

/**
 * Class GameActionSwitchedMessage
 * used to inform the views that the action has been switched
 */
public class GameActionSwitchedMessage extends NetMessage {
    private final Integer ID;
    private final Action action;

    /**
     * Class constructor
     * @param ID the game ID
     * @param action the current action
     */
    public GameActionSwitchedMessage(Integer ID, Action action) {
        this.ID = ID;
        this.action = action;
    }

    /**
     * @return Integer
     */
    public Integer getID() {
        return ID;
    }

    /**
     * @return Action
     */
    public Action getAction() {
        return action;
    }
}
