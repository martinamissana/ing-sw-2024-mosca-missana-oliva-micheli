package it.polimi.ingsw.network;

import it.polimi.ingsw.view.TUI.TUI;

public class ClientTUI {
    public static void main(String[] args) {
        new Thread(() -> {
            try {
                TUI tui = new TUI();
                tui.run();
            } catch (NullPointerException e) {
                System.out.println("Connection lost with server.");
                Thread.currentThread().interrupt();
            }

        }).start();
    }
}
