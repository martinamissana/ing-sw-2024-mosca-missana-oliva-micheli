package it.polimi.ingsw.model.observer.events;

/**
 * LobbyDeletedEvent class<br>
 * Extends abstract class Event<br>
 * Used to notify the virtual view of the deletion of a lobby
 */
public class LobbyDeletedEvent extends Event{
    private final Integer ID;

    /**
     * Class constructor
     * @param ID lobbyID
     */
    public LobbyDeletedEvent(Integer ID) { this.ID = ID; }

    /**
     * @return lobbyID
     */
    public Integer getID() { return ID; }
}
