package it.polimi.ingsw.network;

public interface Connectable {
    void send(String string);
    String receive();
    void disconnect();
}
