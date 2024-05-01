package it.polimi.ingsw.network.RMI;

import java.rmi.Remote;

public interface RemoteInterface extends Remote {
    //all the methods client can call, and every method throws RemoteException
    public void login();
    public void heartbeat();
}
