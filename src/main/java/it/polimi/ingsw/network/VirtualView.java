package it.polimi.ingsw.network;

import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.observer.events.Event;
import it.polimi.ingsw.model.observer.Observer;
import it.polimi.ingsw.model.observer.events.LoginEvent;

import java.io.IOException;

public interface VirtualView extends Runnable, Observer {
    void update(LoginEvent event) throws IOException, NicknameAlreadyTakenException;

    public void update(Event event) throws IOException;
    public void run();
}
