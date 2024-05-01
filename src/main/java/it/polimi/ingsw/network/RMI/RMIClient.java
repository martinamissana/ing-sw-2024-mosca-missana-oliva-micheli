package it.polimi.ingsw.network.RMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {
    Registry registry= LocateRegistry.getRegistry();
    String remoteObjectName="RMIServer";
    RemoteInterface RMIServer=(RemoteInterface) registry.lookup(remoteObjectName);
    public RMIClient() throws RemoteException, NotBoundException {
    }

    public void sendHeartbeat(){
    // create a thread that every bunch of seconds throws an heartbeat
    }
}
