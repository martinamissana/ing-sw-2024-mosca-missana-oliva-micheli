package it.polimi.ingsw.network;

import it.polimi.ingsw.model.exceptions.LobbyDoesNotExistsException;
import it.polimi.ingsw.model.exceptions.PawnAlreadyTakenException;
import it.polimi.ingsw.model.player.Pawn;
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
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                }).start();

                new Thread(() -> {
                    try {
                        client.getCurrentStatus();
                        client.createLobby(2);
                        TimeUnit.SECONDS.sleep(3);
                        client.choosePawn(client.getID(), Pawn.RED);
                        //client.leaveLobby();
                        //new CLIGame(c,client);
                    } catch (IOException | LobbyDoesNotExistsException | PawnAlreadyTakenException |
                             InterruptedException e) {
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
