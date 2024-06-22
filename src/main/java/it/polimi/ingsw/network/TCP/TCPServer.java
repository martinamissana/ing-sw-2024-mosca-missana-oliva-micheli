package it.polimi.ingsw.network.TCP;

import it.polimi.ingsw.controller.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Class of the server that handles all the TCP connections
 */
public class TCPServer {
    private final int port;
    ExecutorService executor;
    Controller c;

    /**
     * Class constructor -
     * accepts the connections and creates a virtual view for each one submitting it to a thread
     *
     * @param port the connection port
     * @param c    the linked Controller
     */
    public TCPServer(int port, Controller c) {
        this.port = port;
        this.executor = Executors.newCachedThreadPool();
        this.c = c;
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                TCPVirtualView vv = new TCPVirtualView(socket, c);
                executor.submit(vv);
            } catch (IOException e) {
                break;
            }
        }
        executor.shutdown();
    }
}

