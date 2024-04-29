package it.polimi.ingsw.model.observer.events;

public abstract class Event {
    EventType eventType;

    public EventType getEventType() {
        return eventType;
    }
}
