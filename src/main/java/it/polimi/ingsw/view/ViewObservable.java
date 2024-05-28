package it.polimi.ingsw.view;

import it.polimi.ingsw.network.netMessage.NetMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewObservable<T> {
    private final List<ViewObserver> observers = new ArrayList<>();

    public void addObserver(ViewObserver observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    public void removeObserver(ViewObserver observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    public void notify(NetMessage m) throws IOException {
        List<ViewObserver> l = new ArrayList<>(observers);
        synchronized (observers) {
            for(ViewObserver observer : l){
                observer.update(m);
            }
        }
    }
}
