package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.LobbyJoinedMessage;
import it.polimi.ingsw.network.netMessage.s2c.LobbyCreatedMessage;
import it.polimi.ingsw.network.netMessage.s2c.LobbyLeftMessage;
import it.polimi.ingsw.network.netMessage.s2c.LoginMessage;
import it.polimi.ingsw.view.GUI.Lobby.LobbyCreationScreenBuilder;
import it.polimi.ingsw.view.GUI.Lobby.LobbyScreenBuilder;
import it.polimi.ingsw.view.GUI.Lobby.LobbySelectionScreenBuilder;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewObserver;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Builder;

import java.io.IOException;

public class LayoutBuilder implements Builder<Region>, ViewObserver {

    private View view;

    private final LoginScreenBuilder loginScreenBuilder;
    private final MainMenuBuilder mainMenuBuilder;
    private LobbyCreationScreenBuilder lobbyCreationScreenBuilder;
    private LobbySelectionScreenBuilder lobbySelectionScreenBuilder;
    private LobbyScreenBuilder lobbyScreenBuilder;

    private final Region topBar;
    private final BorderPane mainPane = new BorderPane();
    private Region loginScreen;
    private Region mainMenu;
    private Region lobbyCreationScreen;
    private Region lobbySelectionScreen;
    private Region lobbyScreen;
    private Region gameScreen;

    private final Runnable setMainMenu;
    private final Runnable setLobbyCreation;
    private final Runnable setLobbySelection;
    private final Runnable setLobbyScreen;
    private Runnable leaveLobbyScreen;
    private final Runnable setGameScreen;

    public LayoutBuilder(Region topBar) {
        this.topBar = topBar;

        setMainMenu = () -> mainPane.setCenter(mainMenu);
        setLobbyCreation = () -> mainPane.setCenter(lobbyCreationScreen);
        setLobbySelection = () -> {
            mainPane.setCenter(lobbySelectionScreen);
            lobbySelectionScreenBuilder.refresh(); };
        setLobbyScreen = () -> {
            mainPane.setCenter(lobbyScreen);
            lobbyScreenBuilder.refresh(); };
        setGameScreen = () -> mainPane.setCenter(gameScreen);

        this.loginScreenBuilder = new LoginScreenBuilder(this::addThisToViewObservers, this::removeThisFromViewObservers);
        this.mainMenuBuilder = new MainMenuBuilder(setLobbyCreation, setLobbySelection);
    }

    @Override
    public Region build() {

        mainPane.setBackground(Background.fill(Color.BLACK));
        mainPane.setPadding(Insets.EMPTY);
        Image bgImage = new Image(String.valueOf(getClass().getResource("/fxml/bg.png")));
        Background background = new Background(new BackgroundImage(bgImage, null, null, null, null));
        mainPane.setBackground(background);

        // set the top bar and login screen
        mainPane.setTop(topBar);

        loginScreen = loginScreenBuilder.build();
        mainMenu = mainMenuBuilder.build();
        mainPane.setCenter(loginScreen);
        return mainPane;
    }

    private void addThisToViewObservers() { ViewSingleton.getInstance().getView().addObserver(this); }

    private void removeThisFromViewObservers() { ViewSingleton.getInstance().getView().removeObserver(this); }

    @Override
    public void update(NetMessage message) throws IOException {
        switch (message) {
            case LoginMessage m -> { // initialize screens at login
                if (view == null) view = ViewSingleton.getInstance().getView();
                if (!m.getNickname().equals(view.getNickname())) return;

                lobbyCreationScreenBuilder = new LobbyCreationScreenBuilder(setMainMenu);
                lobbySelectionScreenBuilder = new LobbySelectionScreenBuilder(setMainMenu);

                Platform.runLater(() -> {
                    lobbyCreationScreen = lobbyCreationScreenBuilder.build();
                    lobbySelectionScreen = lobbySelectionScreenBuilder.build();
                    setMainMenu.run();
                });
            }
            case LobbyCreatedMessage m -> { // if you create a lobby, go to lobby screen
                if (!m.getCreator().equals(view.getPlayer())) return;

                leaveLobbyScreen = null; // sends you back to lobby selection (or to main menu if you created the lobby)
                if (mainPane.getCenter().equals(lobbyCreationScreen))
                    leaveLobbyScreen = setMainMenu;
                else if (mainPane.getCenter().equals(lobbySelectionScreen)) // this if isn't really needed
                    leaveLobbyScreen = setLobbySelection;

                lobbyScreenBuilder = new LobbyScreenBuilder();
                lobbyScreen = lobbyScreenBuilder.build();
                Platform.runLater(setLobbyScreen);
            }
            case LobbyJoinedMessage m -> { // if you join a lobby, go to lobby screen
                if (!m.getPlayer().equals(view.getPlayer())) return;

                leaveLobbyScreen = null; // sends you back to lobby selection (or to main menu if you created the lobby)
                if (mainPane.getCenter().equals(lobbyCreationScreen))
                    leaveLobbyScreen = setMainMenu;
                else if (mainPane.getCenter().equals(lobbySelectionScreen)) // this if isn't really needed
                    leaveLobbyScreen = setLobbySelection;

                lobbyScreenBuilder = new LobbyScreenBuilder();
                lobbyScreen = lobbyScreenBuilder.build();
                Platform.runLater(setLobbyScreen);
            }
            case LobbyLeftMessage m -> {
                if (!m.getPlayer().equals(view.getPlayer())) return;
                Platform.runLater(leaveLobbyScreen);
            }
            default -> {}
        }
    }
}