package it.polimi.ingsw.network;

import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.TUI.TUI;
import javafx.application.Application;

import java.util.Scanner;

import static java.lang.System.exit;

/**
 * The main class that starts either the TUI or the GUI on the client side
 */
public class Client {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert your interface type: [TUI|GUI]");
        String choice = scanner.nextLine();
        Scanner input = new Scanner(System.in);
        if (choice.equalsIgnoreCase("TUI")) {
            new Thread(() -> {
                try {
                    TUI tui = new TUI();
                    tui.run();
                } catch (NullPointerException e) {
                    System.out.println("Connection lost with server.");
                    Thread.currentThread().interrupt();
                }

            }).start();
        } else if (choice.equalsIgnoreCase("GUI")) {
            Application.launch(GUI.class, args);
        } else exit(1);
    }

}

