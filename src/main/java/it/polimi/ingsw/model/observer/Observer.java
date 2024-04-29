package it.polimi.ingsw.model.observer;

import it.polimi.ingsw.model.game.GameHandler;
import it.polimi.ingsw.model.observer.events.Event;

public interface Observer<T> {
    public void update(Event event);
}
