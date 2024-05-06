package it.polimi.ingsw.network.TCP;

import it.polimi.ingsw.view.CLI.CLI;
import it.polimi.ingsw.view.TCPView;

import java.io.IOException;

public class OtherClient {
    public static void main(String[] args) throws IOException {
        String choice = "TCP";
        if(choice.equals("TCP")) {
            TCPView client1 = new TCPView("127.0.0.1", 4321);
            try {
                client1.login("Bea");
                new Thread(() -> {
                    try {
                        client1.startClient();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                }).start();

                new Thread(() -> {

                        /*TimeUnit.SECONDS.sleep(5);
                        client1.getCurrentStatus();
                        TimeUnit.SECONDS.sleep(5);
                        client1.joinLobby(0);
                        TimeUnit.SECONDS.sleep(3);
                        client1.choosePawn(client1.getID(), Pawn.RED);
                        TimeUnit.SECONDS.sleep(3);
                        client1.choosePawn(client1.getID(), Pawn.BLUE);
                        TimeUnit.SECONDS.sleep(3);*/
                    CLI cli=new CLI(client1);
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
