package it.polimi.ingsw.model.observer;


import it.polimi.ingsw.model.observer.events.Event;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable<T> {

    private final List<Observer<T>> observers = new ArrayList<>();

    public void addObserver(Observer<T> observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    public void removeObserver(Observer<T> observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    protected void notify(Event event){
        synchronized (observers) {
            for(Observer<T> observer : observers){
                observer.update(event);
            }
        }
    }
}
