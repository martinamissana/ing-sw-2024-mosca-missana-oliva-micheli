package it.polimi.ingsw.network;

import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.view.CLI.CLI;
import it.polimi.ingsw.view.RMIView;
import it.polimi.ingsw.view.TCPView;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException, NotBoundException, NotBoundException {
        Scanner scanner=new Scanner(System.in);
        System.out.println("Insert your connection type: [TCP|RMI]");
        String choice =scanner.nextLine();
        Scanner input = new Scanner(System.in);
        if (choice.equals("TCP")) {
            TCPView client = new TCPView("127.0.0.1", 4321);
            try {
                System.out.print("\u001B[38;2;255;165;0m" + "\n[+] " + "\u001B[0m" + "Insert username:" + "\u001B[38;2;255;165;0m" + "\n[-] " + "\u001B[0m");
                client.login(input.nextLine());
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
                    CLI cli = new CLI(client);
                    cli.run();
                }).start();

            } catch (IOException e) {
                System.err.println(e.getMessage());
            } catch (FullLobbyException e) {
                throw new RuntimeException(e);
            } catch (NicknameAlreadyTakenException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            RMIView client = new RMIView();
            try {
                client.login("Carlos");
                System.out.println("Client created");
            } catch (NicknameAlreadyTakenException e) {
                throw new RuntimeException(e);
            }
            new Thread(() -> {
                CLI cli = new CLI(client);
                cli.run();
            }).start();
        }
    }
}
