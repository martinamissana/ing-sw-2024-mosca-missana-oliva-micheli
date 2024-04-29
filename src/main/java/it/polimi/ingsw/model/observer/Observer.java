package it.polimi.ingsw.model.observer;

import it.polimi.ingsw.model.game.GameHandler;
import it.polimi.ingsw.model.observer.events.Event;
import it.polimi.ingsw.network.exceptions.ConnectionException;

import java.io.IOException;

public interface Observer<T> {
    public void update(Event event) throws IOException, ConnectionException;
}
