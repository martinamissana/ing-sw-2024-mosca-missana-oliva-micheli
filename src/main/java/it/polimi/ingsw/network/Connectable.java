package it.polimi.ingsw.network;

import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.exceptions.ConnectionException;

import java.io.IOException;

public interface Connectable {
    public void send(NetMessage message) throws IOException, ConnectionException;
    public NetMessage receive() throws InterruptedException, ConnectionException;
    public void disconnect();
}
