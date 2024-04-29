package it.polimi.ingsw.network;

import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.exceptions.ConnectionException;

import java.io.IOException;

public interface Connectable {
    public void send(NetMessage message) throws IOException, ConnectionException;
    public void receive(NetMessage message) throws InterruptedException, ConnectionException, NicknameAlreadyTakenException;
    public void disconnect();
}
