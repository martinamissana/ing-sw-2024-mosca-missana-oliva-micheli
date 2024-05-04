package it.polimi.ingsw.network;

import it.polimi.ingsw.model.exceptions.LobbyDoesNotExistsException;
import it.polimi.ingsw.view.TCPView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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

                }).start();

                new Thread(() -> {
                    try {
                        TimeUnit.SECONDS.sleep(10);
                        client.createLobby(3);
                        //new CLIGame(c,client);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (LobbyDoesNotExistsException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
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
