package it.polimi.ingsw.view.GUI.Lobby;

import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.LobbyJoinedMessage;
import it.polimi.ingsw.network.netMessage.s2c.LobbyLeftMessage;
import it.polimi.ingsw.view.GUI.ViewSingleton;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewObserver;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Builder;

import java.io.IOException;

public class LobbyPlayersWidget implements Builder<Region>, ViewObserver {

    private final View view = ViewSingleton.getInstance().getView();
    private Lobby lobby;

    private final VBox playerWidget = new VBox();

    @Override
    public Region build() {
        view.addObserver(this);
        
        playerWidget.setBackground(new Background(new BackgroundFill(Color.valueOf("#FFFFFFBF"), new CornerRadii(12.5), null)));
        playerWidget.setPadding(new Insets(5, 5, 5, 5));
        playerWidget.setSpacing(5);
        playerWidget.setAlignment(Pos.CENTER);
        playerWidget.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        refresh();
        return playerWidget;
    }

    public void refresh() {
        lobby = view.getLobbies().get(view.getID());
        if (lobby == null) return;
        playerWidget.getChildren().clear();
        for (Player player : lobby.getPlayers())
            addPlayerBox(player.getNickname());
        while (playerWidget.getChildren().size() < lobby.getNumOfPlayers())
            playerWidget.getChildren().add(newEmptyBox());
    }

    private void addPlayerBox(String nickname) {
        playerWidget.getChildren().removeIf(box -> box.getId().equals("emptyBox"));
        playerWidget.getChildren().add(newPlayerBox(nickname));
        while (playerWidget.getChildren().size() < lobby.getNumOfPlayers())
            playerWidget.getChildren().add(newEmptyBox());
    }

    private void removePlayerBox(String nickname) {
        playerWidget.getChildren().removeIf(box -> box.getId().equals(nickname + "Player"));
        playerWidget.getChildren().add(newEmptyBox());
    }

    private Region newPlayerBox(String nickname) {
        HBox playerBox = new HBox();
        playerBox.setId(nickname + "Player"); // this is to prevent players called emptyBox from breaking everything
        playerBox.setBackground(new Background(new BackgroundFill(Color.valueOf("#FFFFFF"), new CornerRadii(7), null)));
        playerBox.setMinHeight(40);
        playerBox.setMinWidth(110);
        playerBox.setAlignment(Pos.CENTER);

        TextField nicknameField = new TextField();
        nicknameField.setEditable(false);
        nicknameField.setFocusTraversable(false);
        nicknameField.setMouseTransparent(true);
        nicknameField.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        nicknameField.setText(nickname);
        nicknameField.setAlignment(Pos.CENTER);
        playerBox.getChildren().add(nicknameField);
        return playerBox;
    }

    private Region newEmptyBox() {
        HBox emptyBox = new HBox();
        emptyBox.setId("emptyBox");
        emptyBox.setBackground(new Background(new BackgroundFill(Color.valueOf("#FFFFFF"), new CornerRadii(7), null)));
        emptyBox.setMinHeight(40);
        emptyBox.setMinWidth(110);
        return emptyBox;
    }

    @Override
    public void update(NetMessage message) throws IOException {
        switch (message) {
            case LobbyJoinedMessage m -> Platform.runLater(() -> { // if someone else joins the lobby
                if (m.getID().equals(view.getID()) && !m.getPlayer().getNickname().equals(view.getNickname()))
                    addPlayerBox(m.getPlayer().getNickname());
            });
            case LobbyLeftMessage m -> Platform.runLater(() -> removePlayerBox(m.getPlayer().getNickname()));
            default -> {}
        }
    }
}
