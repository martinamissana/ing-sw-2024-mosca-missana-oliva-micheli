package it.polimi.ingsw.model.observer;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.observer.events.Event;
import it.polimi.ingsw.model.observer.events.LoginEvent;
import it.polimi.ingsw.network.Connectable;
import it.polimi.ingsw.network.exceptions.ConnectionException;
import it.polimi.ingsw.network.netMessage.LoginMessage;

import java.io.IOException;

public class VirtualView implements Observer{
    private Controller c;
    private Connectable connection;

    @Override
    public void update(Event event) throws IOException, ConnectionException {
        LoginMessage m= new LoginMessage(((LoginEvent) event).getNickname());
        connection.send(m);
    }
    public void Login(String nickname) throws NicknameAlreadyTakenException {
        c.login(nickname);
    }


}
