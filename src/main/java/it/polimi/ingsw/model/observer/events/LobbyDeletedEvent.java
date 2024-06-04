package it.polimi.ingsw.model.observer.events;

/**
 * LobbyDeletedEvent class
 * Extends abstract class Event
 * Used to notify the virtual view of the deletion of a lobby
 */
public class LobbyDeletedEvent extends Event{
    private final Integer ID;

    /**
     * class constructor
     * @param ID lobbyID
     */
    public LobbyDeletedEvent(Integer ID) {
        this.ID = ID;
    }

    /**
     * getter
     * @return lobbyID
     */
    public Integer getID() {
        return ID;
    }
}
