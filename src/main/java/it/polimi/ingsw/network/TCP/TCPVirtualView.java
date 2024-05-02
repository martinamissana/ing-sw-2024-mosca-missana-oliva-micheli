package it.polimi.ingsw.network.TCP;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.observer.Observable;
import it.polimi.ingsw.model.observer.Observer;
import it.polimi.ingsw.model.observer.events.Event;
import it.polimi.ingsw.model.observer.events.LoginEvent;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.MyNickname;
import it.polimi.ingsw.network.netMessage.s2c.LoginMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class TCPVirtualView implements Observer {
    private final Controller c;


    public TCPVirtualView(Controller c) {
        this.c = c;
    }

    @Override
    public void update(Event event) throws IOException{
        switch (event.getEventType()) {
            case LOGIN -> {
               // ObjectOutputStream temp= new ObjectOutputStream();;
                LoginMessage m = new LoginMessage(((LoginEvent) event).getNickname());
                //objectOutputStream.writeObject(m);
            }
            default -> throw new IllegalStateException("Unexpected value: " + event.getEventType());
        }
    }

    private void elaborate(NetMessage message) throws InterruptedException, NicknameAlreadyTakenException, IOException {
        switch (message) {
            case MyNickname m -> {
                c.login(m.getNickname());
            }
            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
    }



}
