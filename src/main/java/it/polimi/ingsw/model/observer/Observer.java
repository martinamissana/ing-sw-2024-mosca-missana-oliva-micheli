package it.polimi.ingsw.model.observer;

import it.polimi.ingsw.model.observer.events.Event;

import java.io.IOException;

public interface Observer<T> {
    public void update(Event event) throws IOException, ConnectionException;
}
