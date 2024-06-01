package it.polimi.ingsw.view.GUI.Lobby;

import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.s2c.LobbyCreatedMessage;
import it.polimi.ingsw.network.netMessage.s2c.LobbyDeletedMessage;
import it.polimi.ingsw.view.GUI.ViewSingleton;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewObserver;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Builder;

import java.io.IOException;

public class LobbyListWidget implements Builder<Region>, ViewObserver {

    private final View view = ViewSingleton.getInstance().getView();

    private final VBox lobbyBoxes = new VBox();

    public LobbyListWidget() {}

    @Override
    public Region build() {
        view.addObserver(this);

        lobbyBoxes.setBackground(new Background(new BackgroundFill(Color.valueOf("#FFFFFFBF"), new CornerRadii(12.5), null)));
        lobbyBoxes.setPadding(new Insets(5, 5, 5, 5));
        lobbyBoxes.setSpacing(5);
        lobbyBoxes.setAlignment(Pos.CENTER);
        lobbyBoxes.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // initialize lobbies
        refresh();

        return lobbyBoxes;
    }

    public void refresh() {
        lobbyBoxes.getChildren().clear();

        for (Lobby lobby: view.getLobbies().values()) {
            LobbyBoxBuilder lobbyBoxBuilder = new LobbyBoxBuilder(lobby);
            lobbyBoxBuilder.refresh();
            lobbyBoxes.getChildren().add(lobbyBoxBuilder.build());
        }
    }

    private void addLobbyBox(Lobby lobby) { lobbyBoxes.getChildren().add(new LobbyBoxBuilder(lobby).build()); }

    private void removeLobbyBox(int lobbyID) { lobbyBoxes.getChildren().removeIf(box -> box.getId().equals(String.valueOf(lobbyID))); }

    @Override
    public void update(NetMessage message) throws IOException {
        switch (message) {
            case LobbyCreatedMessage m -> Platform.runLater(() -> addLobbyBox(m.getLobby()));
            case LobbyDeletedMessage m -> Platform.runLater(() -> removeLobbyBox(m.getID()));
            default -> {}
        }
    }
}
