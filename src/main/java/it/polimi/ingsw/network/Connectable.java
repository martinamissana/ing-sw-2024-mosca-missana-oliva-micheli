package it.polimi.ingsw.network;

import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.network.exceptions.ConnectionException;

import java.io.IOException;

public interface Connectable {
    public void send(Message message) throws IOException, ConnectionException;
    public Message receive() throws InterruptedException, ConnectionException;
    public void disconnect();
}
