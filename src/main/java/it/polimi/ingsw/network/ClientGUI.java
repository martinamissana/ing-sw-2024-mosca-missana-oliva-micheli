package it.polimi.ingsw.network;

import it.polimi.ingsw.view.GUI.GUI;

import javafx.application.Application;

/**
 * Class ClientGUI
 * launches the GUI
 */
public class ClientGUI {
    public static void main(String[] args) {
        System.setProperty("java.rmi.server.hostname", args[0]);
        Application.launch(GUI.class, args);
    }
}
