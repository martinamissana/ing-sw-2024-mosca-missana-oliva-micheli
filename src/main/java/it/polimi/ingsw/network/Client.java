package it.polimi.ingsw.network;

import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.network.RMI.ClientRemoteInterface;
import it.polimi.ingsw.view.CLI.CLI;
import it.polimi.ingsw.view.RMIView;
import it.polimi.ingsw.view.TCPView;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static java.lang.System.exit;

public class Client {
    public static void main(String[] args) throws IOException, NotBoundException, NotBoundException {
        Scanner scanner=new Scanner(System.in);
        System.out.println("Insert your connection type: [TCP|RMI]");
        String choice = scanner.nextLine();
        Scanner input = new Scanner(System.in);
        if (choice.equalsIgnoreCase("TCP")) {
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
                    try {
                        do{
                            client.heartbeat();
                            TimeUnit.SECONDS.sleep(3);
                        }while(!client.getSocket().isClosed());
                    } catch (IOException | ClassNotFoundException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();

                new Thread(() -> {
                    CLI cli = new CLI(client);
                    cli.run();
                }).start();

            } catch (IOException e) {
                System.err.println(e.getMessage());
            } catch (NicknameAlreadyTakenException e) {
                throw new RuntimeException(e);
            }
        } else if (choice.equalsIgnoreCase("RMI")) {
            RMIView client = new RMIView();
            System.out.print("\u001B[38;2;255;165;0m" + "\n[+] " + "\u001B[0m" + "Insert username:" + "\u001B[38;2;255;165;0m" + "\n[-] " + "\u001B[0m");
            try {
                client.login(input.nextLine());
            } catch (NicknameAlreadyTakenException e) {
                throw new RuntimeException(e);
            }
            Registry registry = LocateRegistry.createRegistry(1099);
            ClientRemoteInterface view = (ClientRemoteInterface) UnicastRemoteObject.exportObject(client,1099);
            registry.rebind("Client", view);
            System.out.println("Remote Client is ready");
            new Thread(() -> {
                CLI cli = new CLI(client);
                cli.run();
            }).start();
        } else exit(1);
    }
}
