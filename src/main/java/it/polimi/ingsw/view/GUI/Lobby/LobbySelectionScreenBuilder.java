package it.polimi.ingsw.view.GUI.Lobby;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.util.Builder;

public class LobbySelectionScreenBuilder implements Builder<Region> {

    private final LobbyListWidget lobbyListWidgetBuilder = new LobbyListWidget();

    private final Runnable goToMainMenu;

    public LobbySelectionScreenBuilder(Runnable goToMainMenu) { this.goToMainMenu = goToMainMenu; }

    @Override
    public Region build() {

        Button backButton = new Button("back");
        backButton.setOnAction(actionEvent -> goToMainMenu.run());

        Region lobbyList = lobbyListWidgetBuilder.build();

        StackPane stackPane = new StackPane(new AnchorPane(backButton), lobbyList);
        AnchorPane.setTopAnchor(backButton, 5.0);
        AnchorPane.setLeftAnchor(backButton, 5.0);
        stackPane.setAlignment(Pos.TOP_CENTER);

        return stackPane;
    }

    public void refresh() { lobbyListWidgetBuilder.refresh(); }
}
