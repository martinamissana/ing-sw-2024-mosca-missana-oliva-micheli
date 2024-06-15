package it.polimi.ingsw.view;

import it.polimi.ingsw.network.netMessage.NetMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class extended by observable classes (client side)
 */
public class ViewObservable {
    private final List<ViewObserver> observers = new ArrayList<>();

    /**
     * adds an observer to the list
     * @param observer the observer to add
     */
    public void addObserver(ViewObserver observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * removes an observer to the list
     * @param observer the observer to remove
     */
    public void removeObserver(ViewObserver observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * notifies all observers in the observer list
     * @param m the message to be notified
     * @throws IOException produced by failed or interrupted I/O operations
     */
    public void notify(NetMessage m) throws IOException {
        List<ViewObserver> l = new ArrayList<>(observers);
        synchronized (observers) {
            for(ViewObserver observer : l){
                observer.update(m);
            }
        }
    }
}
