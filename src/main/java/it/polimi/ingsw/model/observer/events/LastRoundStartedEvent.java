package it.polimi.ingsw.model.observer.events;

/**
 * LastRoundStartedEvent class
 * Extends abstract class Event
 * Used to notify the virtual view of the last round beginning
 */
public class LastRoundStartedEvent  extends Event{
    private final Integer ID;

    /**
     * class constructor
     * @param ID gameID
     */
    public LastRoundStartedEvent(Integer ID) {
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
