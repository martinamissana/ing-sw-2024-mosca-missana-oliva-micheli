package it.polimi.ingsw.model.observer;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.observer.events.Event;
import it.polimi.ingsw.model.observer.events.LoginEvent;
import it.polimi.ingsw.network.Connectable;
import it.polimi.ingsw.network.exceptions.ConnectionException;
import it.polimi.ingsw.network.netMessage.c2s.MyNickname;
import it.polimi.ingsw.network.netMessage.s2c.LoginMessage;
import it.polimi.ingsw.network.netMessage.NetMessage;

import java.io.IOException;

public class VirtualView implements Observer, Runnable{
    private Controller c;
    private Connectable connection;

    public VirtualView(Controller c, Connectable connection) {
        this.c = c;
        this.connection = connection;
    }

    @Override
    public void update(Event event) throws IOException, ConnectionException {
        switch (event.getEventType()) {
            case LOGIN -> {
                LoginMessage m = new LoginMessage(((LoginEvent) event).getNickname());
                connection.send(m);
            }
        }
    }

    private void elaborate(NetMessage message) throws InterruptedException, ConnectionException, NicknameAlreadyTakenException {
        switch (message) {
            case MyNickname m -> {
               c.login(m.getNickname());
            }
            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
    }


    @Override
    public void run() {

    }
}
