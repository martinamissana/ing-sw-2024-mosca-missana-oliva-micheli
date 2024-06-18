package it.polimi.ingsw.network;

import it.polimi.ingsw.view.GUIFX.GUI;
import it.polimi.ingsw.view.TUI.TUI;
import javafx.application.Application;

import java.util.Scanner;

import static java.lang.System.exit;

public class ClientTUI {
    public static void main(String[] args) {
        new Thread(() -> {
            try {
                TUI tui = new TUI();
                tui.run();
            } catch (NullPointerException e) {
                System.out.println("connection lost with server");
                Thread.currentThread().interrupt();
            }

        }).start();
    }
}
