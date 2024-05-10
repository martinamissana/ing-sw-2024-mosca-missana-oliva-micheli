package it.polimi.ingsw.network.TCP;

import it.polimi.ingsw.controller.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


    public class TCPServer {
        private final int port;
        ExecutorService executor;
        Controller c;
        public TCPServer(int port, Controller c) {
            this.port = port;
            this.executor = Executors.newCachedThreadPool();
            this.c=c;
            ServerSocket serverSocket;
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                System.err.println(e.getMessage());
                return;
            }
            System.out.println("Server ready");
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("New connection!");
                    executor.submit(new TCPVirtualView(socket,c));

                } catch(IOException e) {
                    break; //if a socket is closed
                }
            }
            executor.shutdown();
        }
    }

