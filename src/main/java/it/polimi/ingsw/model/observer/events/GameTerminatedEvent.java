package it.polimi.ingsw.model.observer.events;

public class GameTerminatedEvent extends Event{
    private final Integer ID;

    public GameTerminatedEvent(Integer ID) {
        this.ID = ID;
    }

    public Integer getID() {
        return ID;
    }
}
