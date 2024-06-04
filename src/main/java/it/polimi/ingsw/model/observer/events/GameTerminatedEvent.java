package it.polimi.ingsw.model.observer.events;

/**
 * GameTerminatedEvent class
 * Extends abstract class Event
 * Used to notify the virtual view of the clients that the game is finished
 */
public class GameTerminatedEvent extends Event{
    private final Integer ID;

    /**
     * class constructor
     * @param ID gameID
     */
    public GameTerminatedEvent(Integer ID) {
        this.ID = ID;
    }

    /**
     * getter
     * @return gameID
     */
    public Integer getID() {
        return ID;
    }
}
