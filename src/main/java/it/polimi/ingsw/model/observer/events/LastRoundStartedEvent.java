package it.polimi.ingsw.model.observer.events;

public class LastRoundStartedEvent  extends Event{
    private final Integer ID;

    public LastRoundStartedEvent(Integer ID) {
        this.ID = ID;
    }

    public Integer getID() {
        return ID;
    }
}
