package it.polimi.ingsw.network;

import it.polimi.ingsw.view.TCPView;

import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException {

        if(args[0].equals("TCP")) {
            TCPView client = new TCPView("127.0.0.1", 5555);
            try {
                client.startClient();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println(e.getMessage());
            }
        }
        else{
            //RMI
        }
    }
}
