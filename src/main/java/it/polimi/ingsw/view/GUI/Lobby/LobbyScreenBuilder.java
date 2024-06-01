package it.polimi.ingsw.view.GUI.Lobby;

import it.polimi.ingsw.controller.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.controller.exceptions.NotConnectedToLobbyException;
import it.polimi.ingsw.controller.exceptions.UnexistentUserException;
import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.GameDoesNotExistException;
import it.polimi.ingsw.model.exceptions.LobbyDoesNotExistsException;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.s2c.GameCreatedMessage;
import it.polimi.ingsw.network.netMessage.s2c.LobbyLeftMessage;
import it.polimi.ingsw.view.GUI.ViewSingleton;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewController;
import it.polimi.ingsw.view.ViewObserver;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.util.Builder;

import java.io.IOException;

public class LobbyScreenBuilder implements Builder<Region> {

    private final View view = ViewSingleton.getInstance().getView();
    private final ViewController viewCon = ViewSingleton.getInstance().getViewController();

    private final LobbyPlayersWidget lobbyPlayersWidget = new LobbyPlayersWidget();
    private final PawnChooserWidget pawnChooserWidget = new PawnChooserWidget();

    public LobbyScreenBuilder() {}

    @Override
    public Region build() {

        Button back = new Button("back");
        back.setOnAction(actionEvent -> {
            try {
                viewCon.checkLeaveLobby();
                view.leaveLobby();
            } catch (NotConnectedToLobbyException | GameAlreadyStartedException | FullLobbyException |
                     LobbyDoesNotExistsException | NicknameAlreadyTakenException | IOException |
                     ClassNotFoundException | GameDoesNotExistException | UnexistentUserException e) {
                throw new RuntimeException(e);
            }
        });
        ToggleButton chatButton = new ToggleButton("chat");

        AnchorPane buttonsPane = new AnchorPane(back, chatButton);
        AnchorPane.setTopAnchor(back, 5.0);
        AnchorPane.setLeftAnchor(back, 5.0);
        AnchorPane.setTopAnchor(chatButton, 5.0);
        AnchorPane.setRightAnchor(chatButton, 5.0);

        HBox widgets = new HBox(lobbyPlayersWidget.build(), pawnChooserWidget.build());
        widgets.setAlignment(Pos.CENTER);
        widgets.setSpacing(50);

        refresh();

        return new StackPane(buttonsPane, widgets);
    }

    public void refresh () {
        lobbyPlayersWidget.refresh();
        pawnChooserWidget.refresh();
    }
}
