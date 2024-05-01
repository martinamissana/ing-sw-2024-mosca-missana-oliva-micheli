package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.observer.events.Event;
import it.polimi.ingsw.model.observer.events.LoginEvent;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.MyNickname;
import it.polimi.ingsw.network.netMessage.s2c.LoginMessage;

import java.io.IOException;

public class SocketVirtualView implements VirtualView {
    private Controller c;


    public SocketVirtualView(Controller c) {
        this.c = c;
    }

    @Override
    public void update(LoginEvent event) throws IOException, NicknameAlreadyTakenException {

    }

    @Override
    public void update(Event event) throws IOException{
        switch (event.getEventType()) {
            case LOGIN -> {
                LoginMessage m = new LoginMessage(((LoginEvent) event).getNickname());
                //send(m);
            }
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


    @Override
    public void run() {

    }
}
