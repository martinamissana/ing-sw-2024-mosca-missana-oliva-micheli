package it.polimi.ingsw.network;

import it.polimi.ingsw.view.CLI.*;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.application.Application;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Scanner;

import static java.lang.System.exit;

public class Client {
    public static void main(String[] args) throws IOException, NotBoundException, NotBoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert your interface type: [TUI|GUI]");
        String choice = scanner.nextLine();
        Scanner input = new Scanner(System.in);
        if (choice.equalsIgnoreCase("TUI")) {
            new Thread(() -> {
                CLI cli = new CLI();
                cli.run();
            }).start();
        } else if (choice.equalsIgnoreCase("GUI")) {
            Application.launch(GUI.class, args);
        } else exit(1);
    }
        /*if (choice.equalsIgnoreCase("TCP")) {
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
                    CLI cli = new CLI(client);
                    cli.run();
                }).start();

            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        } else if (choice.equalsIgnoreCase("RMI")) {
            Registry registry = LocateRegistry.getRegistry();
            System.out.println("RMI registry bindings: ");
            String[] e = registry.list();
            for (String string : e) {
                System.out.println(string);
            }
            String remoteObjectName = "RMIServer";
            RemoteInterface RMIServer = (RemoteInterface) registry.lookup(remoteObjectName);
            ClientRemoteInterface client = new RMIView(RMIServer);
            RMIServer.connect((ClientRemoteInterface) UnicastRemoteObject.exportObject(client,0));
            System.out.print("\u001B[38;2;255;165;0m" + "\n[+] " + "\u001B[0m" + "Insert username:" + "\u001B[38;2;255;165;0m" + "\n[-] " + "\u001B[0m");
            try {
                ((RMIView) client).login(input.nextLine());
            } catch (NicknameAlreadyTakenException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Remote Client is ready");
            new Thread(() -> {
                CLI cli = new CLI((View)client);
                cli.run();
            }).start();
        } else exit(1);
    }*/
}

