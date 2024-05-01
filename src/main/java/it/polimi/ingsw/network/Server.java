package it.polimi.ingsw.network;

import it.polimi.ingsw.network.RMI.RMIImpl;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main() throws RemoteException, AlreadyBoundException{
        RMIImpl RMIServer= new RMIImpl();
        Registry registry= LocateRegistry.getRegistry();
        registry.bind("RMIServer",RMIServer);
        //create server socket

        // accept creating rmiclient or socketclient

    }
}
