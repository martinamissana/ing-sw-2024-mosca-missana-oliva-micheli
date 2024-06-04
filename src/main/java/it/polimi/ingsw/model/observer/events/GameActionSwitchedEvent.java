package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.game.Action;

/**
 * GameActionSwitchedEvent class
 * Extends abstract class Event
 * Used to notify the virtual view of the client when the action type is changed [draw-play]
 */
public class GameActionSwitchedEvent  extends Event{
    private final Integer ID;
    private final Action action;

    /**
     * class constructor
     * @param ID ID of the game/lobby
     * @param action new type of action
     */
    public GameActionSwitchedEvent(Integer ID, Action action) {
        this.ID = ID;
        this.action = action;
    }

    /**
     * getter
     * @return ID of the game/lobby
     */
    public Integer getID() {
        return ID;
    }

    /**
     * getter
     * @return new type of action
     */
    public Action getAction() {
        return action;
    }
}
