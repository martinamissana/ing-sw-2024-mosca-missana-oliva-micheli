package it.polimi.ingsw.network.RMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIImpl extends UnicastRemoteObject implements RemoteInterface {
    public RMIImpl() throws RemoteException {
    }

    @Override
    public void login() {

    }

    // calls the controller methods implemnting remoteinterface
    @Override
    public void heartbeat(){
        //timeout, if the heartbeat doesn't arrive an exception is thrown
    }
}
