package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.game.GameHandler;
import it.polimi.ingsw.network.RMI.RMIImpl;
import it.polimi.ingsw.network.TCP.TCPServer;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args){

        Controller c=new Controller(new GameHandler());
        new Thread(() -> {
            TCPServer server = new TCPServer(4321);
        }).start();
        new Thread(() -> {
            RMIImpl RMIServer= null;
            try {
                RMIServer = new RMIImpl();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            Registry registry= null;
            try {
                registry = LocateRegistry.getRegistry();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            try {
                registry.bind("RMIServer",RMIServer);
            } catch (RemoteException | AlreadyBoundException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }
}
