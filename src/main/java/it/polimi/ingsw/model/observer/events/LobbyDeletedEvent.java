package it.polimi.ingsw.model.observer.events;

public class LobbyDeletedEvent extends Event{
    private Integer ID;

    public LobbyDeletedEvent(Integer ID) {
        this.ID = ID;
    }

    public Integer getID() {
        return ID;
    }
}
