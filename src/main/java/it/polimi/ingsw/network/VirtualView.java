package it.polimi.ingsw.network;

import it.polimi.ingsw.model.observer.events.Event;
import it.polimi.ingsw.model.observer.Observer;

import java.io.IOException;
import java.rmi.Remote;

public interface VirtualView extends Remote,Runnable, Observer {
    void update(Event event) throws IOException;
    void run();
}