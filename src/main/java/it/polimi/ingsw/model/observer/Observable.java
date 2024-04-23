package it.polimi.ingsw.model.observer;

import it.polimi.ingsw.model.game.GameHandler;

import java.util.ArrayList;

public abstract class Observable {
    private GameHandler data;
    private ArrayList<Listener> listeners = new ArrayList<>();
    public void addListener(Listener ld) {
        listeners.add(ld);
    }
    public void removeListener(Listener ld) {
        listeners.remove(ld);
    }
    protected void updateAll(GameHandler data){
        for(Listener ld : listeners)
            ld.update(data);
    }
    public void setData(GameHandler data) {
        this.data = data;
        for (Listener l : this.listeners) {
            l.update(this.data);
        }
    }
}
