package it.polimi.ingsw.view;

import it.polimi.ingsw.network.netMessage.NetMessage;

import java.io.IOException;

/**
 * Interface implemented by observer classes that receive messages from an observable class (client side)
 */
public interface ViewObserver{
    /**
     * receives a message and handles it
     *
     * @param m the message that they have to handle
     * @throws IOException produced by failed or interrupted I/O operations
     */
public void update(NetMessage m) throws IOException;

}
