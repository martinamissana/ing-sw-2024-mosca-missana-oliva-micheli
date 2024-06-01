package it.polimi.ingsw.view.GUI.Lobby;

import it.polimi.ingsw.controller.exceptions.CannotJoinMultipleLobbiesException;
import it.polimi.ingsw.controller.exceptions.UnexistentUserException;
import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.LobbyDoesNotExistsException;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.LobbyJoinedMessage;
import it.polimi.ingsw.network.netMessage.s2c.LobbyLeftMessage;
import it.polimi.ingsw.view.GUI.ViewSingleton;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewController;
import it.polimi.ingsw.view.ViewObserver;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Builder;

import java.io.IOException;

public class LobbyBoxBuilder implements Builder<Node>, ViewObserver {

    private final View view = ViewSingleton.getInstance().getView();
    private final ViewController viewCon = ViewSingleton.getInstance().getViewController();

    private final Lobby lobby;
    private final SimpleBooleanProperty isFull = new SimpleBooleanProperty(false);

    private final AnchorPane lobbyBox = new AnchorPane();
    private final HBox lobbyInfo = new HBox();
    private final TextField idField = new TextField();
    private final TextField playerCount = new TextField();
    private final Button joinButton = new Button("join");

    public LobbyBoxBuilder(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public Node build() {
        view.addObserver(this);

        lobbyBox.setId(String.valueOf(lobby.getID()));
        lobbyBox.setBackground(new Background(new BackgroundFill(Color.valueOf("#FFFFFF"), new CornerRadii(5), null)));
        lobbyBox.setMinHeight(40);
        lobbyBox.setMinWidth(220);

        idField.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        idField.setAlignment(Pos.CENTER);
        idField.setEditable(false);

        joinButton.disableProperty().bindBidirectional(isFull);
        joinButton.setOnAction(actionEvent -> {
            try {
                viewCon.checkJoinLobby(lobby.getID());
                view.joinLobby(lobby.getID());
            } catch (LobbyDoesNotExistsException | FullLobbyException | CannotJoinMultipleLobbiesException |
                     NicknameAlreadyTakenException | IOException | ClassNotFoundException | UnexistentUserException e) {
                throw new RuntimeException(e);
            }
        });

        refresh();

        AnchorPane.setLeftAnchor(lobbyInfo, 5.0);
        AnchorPane.setRightAnchor(joinButton, 5.0);
        lobbyInfo.getChildren().add(idField);
        lobbyInfo.getChildren().add(playerCount);
        lobbyInfo.setMouseTransparent(true);
        lobbyInfo.setFocusTraversable(false);
        lobbyBox.getChildren().add(lobbyInfo);
        lobbyBox.getChildren().add(joinButton);
        return lobbyBox;
    }

    public int getID() { return lobby.getID(); }

    public void refresh() {
        updatePlayerCount();
        updateJoinable();
    }

    private void updatePlayerCount() { playerCount.setText(lobby.getPlayers().size() + "/" + lobby.getNumOfPlayers()); }

    private void updateJoinable() { isFull.set(lobby.getPlayers().size() == lobby.getNumOfPlayers()); }

    @Override
    public void update(NetMessage message) throws IOException {
        switch (message) {
            case LobbyJoinedMessage m -> { if (m.getID().equals(lobby.getID())) refresh(); }
            case LobbyLeftMessage m -> { if (m.getID().equals(lobby.getID())) refresh(); }
            default -> {}
        }
    }
}
