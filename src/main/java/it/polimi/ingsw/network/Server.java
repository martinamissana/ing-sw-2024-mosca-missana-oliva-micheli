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

        String TCP_Port = args[0];
        //String TCP_Port="4321";

        String RMI_Port = args[1];
        //String RMI_Port="1099";


        new Thread(() -> {

            try {
                InetAddress IP = InetAddress.getLocalHost();
                System.out.println("IP of the server is := " + IP.getHostAddress()); //works on windows
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }

            TCPServer server = new TCPServer(Integer.parseInt(TCP_Port), c);

        }).start();
        new Thread(() -> {
            try {
                RMIServer server = new RMIServer(c);
                Registry registry = LocateRegistry.createRegistry(Integer.parseInt(RMI_Port));
                registry.rebind("RMIServer", server);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }).start();

        System.out.println("TCP port := " + TCP_Port);
        System.out.println("RMI port := " + RMI_Port);
    }
}
