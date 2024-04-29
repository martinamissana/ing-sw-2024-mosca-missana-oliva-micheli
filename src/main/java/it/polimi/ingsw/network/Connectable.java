package it.polimi.ingsw.network;

import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.network.exceptions.ConnectionException;

import java.io.IOException;

public interface Connectable {
    void send(Message message) throws IOException, ConnectionException;
    Message receive() throws InterruptedException, ConnectionException;
    void disconnect();
}
