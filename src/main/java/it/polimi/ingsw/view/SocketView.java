package it.polimi.ingsw.view;

import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.s2c.LoginMessage;

import java.io.IOException;

public class SocketView extends View {

    public SocketView(String nickname) {
        super(nickname);
    }

    private void elaborate(NetMessage message) throws IOException{
        switch (message) {
            case LoginMessage m -> {
                setNickname(m.getNickname());
            }

            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
    }
    public void login(String nickname) throws IOException{
        LoginMessage m= new LoginMessage(nickname);
        //connection.send(m);
    }
}
