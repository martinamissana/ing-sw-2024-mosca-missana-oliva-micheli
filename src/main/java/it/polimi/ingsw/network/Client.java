package it.polimi.ingsw.network;

import it.polimi.ingsw.view.CLI.CLI;
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
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                }).start();

                new Thread(() -> {
                    /*client.getCurrentStatus();
                    client.createLobby(2);
                    TimeUnit.SECONDS.sleep(3);
                    client.choosePawn(client.getID(), Pawn.RED);*/
                    //client.leaveLobby();
                    CLI cli=new CLI(client);
                    cli.run();
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
