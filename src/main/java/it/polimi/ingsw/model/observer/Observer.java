package it.polimi.ingsw.model.observer;

import it.polimi.ingsw.model.observer.events.Event;

import java.io.IOException;

/**
 * Interface implemented by observer classes that receive events from an observable class (server side)
 */
public interface Observer {
    /**
     * receives an event and handles it
     *
     * @param event the event that they have to handle
     * @throws IOException produced by failed or interrupted I/O operations
     */
    public void update(Event event) throws IOException;

}
