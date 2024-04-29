package it.polimi.ingsw.model.observer;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.observer.events.Event;
import it.polimi.ingsw.model.observer.events.LoginEvent;

public class VirtualView implements Observer{
    private Controller c;
    // private Connection;

    @Override
    public void update(Event event) {
        switch (event.getEventType()) {
            case LOGIN -> {
                //connection.send(message)
            }

        }
    }
    public void Login(String nickname) throws NicknameAlreadyTakenException {
        c.login(nickname);
    }
}
