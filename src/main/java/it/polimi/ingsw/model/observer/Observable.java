package it.polimi.ingsw.model.observer;


import it.polimi.ingsw.model.observer.events.Event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class extended by observable classes (server side)
 */
public abstract class Observable {

    private final List<Observer> observers = new ArrayList<>();

    /**
     * adds an observer to the list
     * @param observer the observer to add
     */
    public void addObserver(Observer observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * removes an observer to the list
     * @param observer the observer to remove
     */
    public void removeObserver(Observer observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * notifies all observers in the observer list
     * @param event the event to be notified
     * @throws IOException produced by failed or interrupted I/O operations
     */
    public void notify(Event event) throws IOException {
        List<Observer> l = new ArrayList<>(observers);
        synchronized (observers) {
            for (Observer observer : l) {
                observer.update(event);
            }
        }
    }
}
