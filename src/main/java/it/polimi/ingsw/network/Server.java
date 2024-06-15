package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.game.GameHandler;
import it.polimi.ingsw.network.RMI.RMIServer;
import it.polimi.ingsw.network.TCP.TCPServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * The main class that starts a thread both for the TCP server and the RMI server
 */
public class Server {
    public static void main(String[] args) {

        Controller c = new Controller(new GameHandler());
        new Thread(() -> {

            try {
                InetAddress IP = InetAddress.getLocalHost();
                System.out.println("IP of the server is := " + IP.getHostAddress()); //works on windows
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }

            TCPServer server = new TCPServer(4321, c);

        }).start();
        new Thread(() -> {
            try {
                RMIServer server = new RMIServer(c);
                Registry registry = LocateRegistry.createRegistry(1099);
                registry.rebind("RMIServer", server);
                System.out.println("RMI Server is ready:");
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }
}
