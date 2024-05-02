package it.polimi.ingsw.network;

import it.polimi.ingsw.view.TCPView;

import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException {
        String choice = "TCP";
        if(choice.equals("TCP")) {
            TCPView client = new TCPView("127.0.0.1", 4321);
            try {
                client.login("Carlos");

                new Thread(() -> {
                    try {
                        client.startClient();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    new Thread(() -> {
                        //new CLIGame(c,client);
                    }).start();
                }).start();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        else{
            //RMI
        }
    }
}
