package it.polimi.ingsw.view;

import it.polimi.ingsw.network.netMessage.NetMessage;

import java.io.IOException;

public interface ViewObserver{
public void update(NetMessage m) throws IOException;

}
