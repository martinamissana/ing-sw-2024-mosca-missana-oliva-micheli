package it.polimi.ingsw.model.observer;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.observer.events.Event;
import it.polimi.ingsw.model.observer.events.LoginEvent;
import it.polimi.ingsw.network.Connectable;
import it.polimi.ingsw.network.exceptions.ConnectionException;
import it.polimi.ingsw.network.netMessage.s2c.LoginMessage;
import it.polimi.ingsw.network.netMessage.NetMessage;

import java.io.IOException;

public class VirtualView implements Observer, Connectable{
    private Controller c;

    @Override
    public void update(Event event) throws IOException, ConnectionException {
        LoginMessage m= new LoginMessage(((LoginEvent) event).getNickname());
        send(m);
    }
    public void Receive(String nickname) throws NicknameAlreadyTakenException {
        c.login(nickname);
    }


    @Override
    public void send(NetMessage message) throws IOException, ConnectionException {

    }

    @Override
    public NetMessage receive() throws InterruptedException, ConnectionException {
        return null;
    }

    @Override
    public void disconnect() {

    }
}
