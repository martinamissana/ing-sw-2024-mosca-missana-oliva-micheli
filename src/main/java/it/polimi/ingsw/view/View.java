package it.polimi.ingsw.view;

import it.polimi.ingsw.network.Connectable;
import it.polimi.ingsw.network.exceptions.ConnectionException;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.s2c.LoginMessage;

import java.io.IOException;

public class View implements Runnable {
    private String nickname;
    private Connectable connection;

    public View(String nickname){
        this.nickname=nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void run() {

    }
    private void elaborate(NetMessage message) throws IOException, ConnectionException {
        switch (message) {
            case LoginMessage m -> {
                setNickname(m.getNickname());
            }

            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
    }

    public void login(String nickname) throws IOException, ConnectionException {
        LoginMessage m= new LoginMessage(nickname);
        connection.send(m);
    }
}
