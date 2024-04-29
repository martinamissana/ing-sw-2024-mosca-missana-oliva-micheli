package it.polimi.ingsw.network;

import java.io.IOException;

public interface Connectable {
    void send(String string) throws IOException;
    String receive() throws InterruptedException;
    void disconnect();
}
