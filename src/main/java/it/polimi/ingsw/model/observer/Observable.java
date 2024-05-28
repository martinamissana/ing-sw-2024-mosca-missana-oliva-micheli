package it.polimi.ingsw.model.observer;


import it.polimi.ingsw.model.observer.events.Event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Observable<T> {

    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    public void removeObserver(Observer observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    public void notify(Event event) throws IOException {
        List<Observer> l = new ArrayList<>(observers);
        synchronized (observers) {
            for (Observer observer : l) {
                observer.update(event);
            }
        }
    }
}
