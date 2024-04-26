package it.polimi.ingsw.model.observer;

import it.polimi.ingsw.model.game.GameHandler;

public interface Observer<T> {
    public void update(T update);
}
