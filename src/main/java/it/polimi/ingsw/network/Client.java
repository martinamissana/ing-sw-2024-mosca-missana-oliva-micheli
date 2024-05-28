package it.polimi.ingsw.network;

import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.TUI.TUI;
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
                try{
                    TUI tui = new TUI();
                    tui.run();
                }catch (NullPointerException e) {
                    System.out.println("no server");
                    Thread.currentThread().interrupt();
                }

            }).start();
        } else if (choice.equalsIgnoreCase("GUI")) {
            Application.launch(GUI.class, args);
        } else exit(1);
    }

}

