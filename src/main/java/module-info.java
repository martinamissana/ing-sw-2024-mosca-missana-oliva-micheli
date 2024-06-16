module PSP001 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.google.gson;
    requires java.rmi;
    requires java.logging;
    requires java.desktop;

    opens it.polimi.ingsw.model.card;
    opens it.polimi.ingsw.model.commonItem;
    opens it.polimi.ingsw.model.goal;
    opens it.polimi.ingsw.view.GUI;
    opens it.polimi.ingsw.view.GUIFX;

    exports it.polimi.ingsw.view.GUI;
    exports it.polimi.ingsw.view.GUIFX;
    exports it.polimi.ingsw.network.RMI;
    exports it.polimi.ingsw.controller.exceptions;
    exports it.polimi.ingsw.model.exceptions;
    exports it.polimi.ingsw.controller;
    exports it.polimi.ingsw.model.chat;
    exports it.polimi.ingsw.model.deck;
    exports it.polimi.ingsw.model.card;
    exports it.polimi.ingsw.model.player;
    exports it.polimi.ingsw.view;
    exports it.polimi.ingsw.network.netMessage;
    exports it.polimi.ingsw.model.goal;
    exports it.polimi.ingsw.model.game;
    exports it.polimi.ingsw.model.observer.events;
    exports it.polimi.ingsw.view.GUI.Lobby;
    opens it.polimi.ingsw.view.GUI.Lobby;
}