package it.polimi.ingsw.network.RMI;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.observer.events.Event;
import it.polimi.ingsw.model.observer.events.LoginEvent;
import it.polimi.ingsw.network.VirtualView;

import java.io.IOException;

public class RMIVirtualView implements VirtualView {
    private final Controller c;

    public RMIVirtualView(Controller c) {
        this.c = c;
    }

    @Override
    public void update(LoginEvent event) throws IOException, NicknameAlreadyTakenException {
        c.login(event.getNickname());
    }

    @Override
    public void update(Event event) throws IOException {

    }

    @Override
    public void run() {

    }
}
