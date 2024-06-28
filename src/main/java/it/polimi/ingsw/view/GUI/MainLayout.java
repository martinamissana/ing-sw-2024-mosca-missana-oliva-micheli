package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.game.GamePhase;
import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.LobbyJoinedMessage;
import it.polimi.ingsw.network.netMessage.s2c.*;
import it.polimi.ingsw.view.ViewObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class MainLayout
 * handles the scene shown based on the game phase
 */
public class MainLayout implements ViewObserver {
    @FXML
    private BorderPane layout;
    private Stage stage;
    protected ViewSingleton viewSingleton;      // protected because it's set after login (by LoginController)
    private Lobby lobby;
    private HashMap<Player, Integer> scoreboard;
    private HashMap<Player, Integer> goalsDone;
    private final ArrayList<Player> winners = new ArrayList<>();

    /**
     * @param stage the main stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * handles the scene changing
     * @param fxmlPath fxml file to load
     */
    public void setScene(String fxmlPath) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlPath + ".fxml"));
                AnchorPane center = loader.load();
                layout.setCenter(center);
                if (fxmlPath.equals("Login")) {
                    LoginController controller = loader.getController();
                    controller.setMainLayout(this);
                } else if (fxmlPath.equals("Winners")) {
                    WinnersController controller = loader.getController();
                    controller.setMainLayout(this);

                    if (lobby != null) controller.setPlayersFromLobby(this.lobby);
                    else controller.setWinners(this.scoreboard, this.goalsDone, this.winners);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * adds top bar and sets background color
     */
    public void setStyle() {
        stage.initStyle(StageStyle.UNDECORATED);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TopBar.fxml"));
        try {
            layout.setTop(loader.load());
            TopBarController controller = loader.getController();
            controller.setStage(stage);
            controller.setMainLayout(this);
            layout.setStyle("-fx-background-color: #F2DEBD;");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(NetMessage message) throws IOException {
        switch (message) {
            case LobbyCreatedMessage m -> {
                if (m.getCreator().equals(viewSingleton.getView().getPlayer())) setScene("InLobby");
            }
            case LobbyJoinedMessage m -> {
                if (m.getPlayer().equals(viewSingleton.getView().getPlayer())) setScene("InLobby");
            }
            case LobbyLeftMessage m -> {
                if (m.getPlayer().equals(viewSingleton.getView().getPlayer())) setScene("Open");
                else if (viewSingleton.getView().getGamePhase().equals(GamePhase.PLACING_STARTER_CARD) || viewSingleton.getView().getGamePhase().equals(GamePhase.CHOOSING_SECRET_GOAL)) {
                    this.lobby = viewSingleton.getView().getLobbies().get(viewSingleton.getView().getID());
                }
            }
            case GameCreatedMessage m -> {
                if (m.getID().equals(viewSingleton.getView().getID())) setScene("GameScreen");
            }
            case GameWinnersAnnouncedMessage m -> {
                this.scoreboard = viewSingleton.getView().getScoreboard();
                this.goalsDone = m.getGoalsDone();
                this.winners.addAll(m.getWinners());
            }
            case GameTerminatedMessage ignored -> setScene("Winners");
            default -> {}
        }
    }
}
