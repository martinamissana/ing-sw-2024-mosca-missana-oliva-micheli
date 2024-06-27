package it.polimi.ingsw.model.observer;

import it.polimi.ingsw.model.observer.events.Event;

import java.io.IOException;

/**
 * Interface implemented by observer classes that receive events from an observable class (server side)
 */
public interface Observer {
    /**
     * Receives an event and handles it
     * @param event the event that they have to handle
     * @throws IOException thrown if I/O operations are interrupted or failed
     */
    void update(Event event) throws IOException;
}
