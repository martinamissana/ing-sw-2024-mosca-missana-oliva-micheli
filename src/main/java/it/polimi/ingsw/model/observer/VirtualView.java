package it.polimi.ingsw.model.observer;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.observer.events.Event;
import it.polimi.ingsw.model.observer.events.LoginEvent;
import it.polimi.ingsw.network.Connectable;
import it.polimi.ingsw.network.message.LoginMessage;
import it.polimi.ingsw.network.message.NetMessage;

public class VirtualView implements Observer{
    private Controller c;
    private Connectable connection;

    @Override
    public void update(LoginEvent event) {
        LoginMessage m= new LoginMessage(event.getNickname());
        connection.send((NetMessage)m);
    }
    public void Login(String nickname) throws NicknameAlreadyTakenException {
        c.login(nickname);
    }


}
