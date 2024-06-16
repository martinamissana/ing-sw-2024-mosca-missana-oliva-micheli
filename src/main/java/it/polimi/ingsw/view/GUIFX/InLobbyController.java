package it.polimi.ingsw.view.GUIFX;

import it.polimi.ingsw.controller.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.controller.exceptions.NotConnectedToLobbyException;
import it.polimi.ingsw.controller.exceptions.UnexistentUserException;
import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.GameDoesNotExistException;
import it.polimi.ingsw.model.exceptions.LobbyDoesNotExistsException;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.LobbyJoinedMessage;
import it.polimi.ingsw.network.netMessage.s2c.LobbyLeftMessage;
import it.polimi.ingsw.network.netMessage.s2c.PawnAssignedMessage;
import it.polimi.ingsw.view.ViewObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

public class InLobbyController implements ViewObserver, Initializable {
    @FXML
    Button quitButton;
    @FXML
    Pane players;

    private final ViewSingleton viewSingleton = ViewSingleton.getInstance();
    private Lobby lobby;
    private final Semaphore sem = new Semaphore(0);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewSingleton.getView().addObserver(this);
        this.lobby = viewSingleton.getView().getLobbies().get(viewSingleton.getView().getID());
        updatePlayers();
    }

    public void leaveLobby(MouseEvent mouseEvent) {
        try {
            viewSingleton.getViewController().checkLeaveLobby();
            viewSingleton.getView().leaveLobby();
            sem.acquire();

            viewSingleton.getView().removeObserver(this);
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Open.fxml"));

            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (NotConnectedToLobbyException e) {
            System.out.println("Not in lobby");
        } catch (GameAlreadyStartedException e) {
            System.out.println("Game started");
        } catch (FullLobbyException | LobbyDoesNotExistsException | NicknameAlreadyTakenException |
                 GameDoesNotExistException ignored) {}
        catch (IOException | ClassNotFoundException | UnexistentUserException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void updatePlayers() {
        Platform.runLater(() -> {
            try {
                players.getChildren().clear(); // Clear the children before adding new ones
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Scoreboard.fxml"));
                players.getChildren().add(loader.load());
                ScoreboardController controller = loader.getController();
                controller.setPlayers(lobby);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void update(NetMessage message) throws IOException {
        switch (message) {
            case LobbyLeftMessage m -> {
                if (m.getPlayer().equals(viewSingleton.getView().getPlayer())) sem.release();
                else updatePlayers();
            }
            case LobbyJoinedMessage ignored -> updatePlayers();
            case PawnAssignedMessage ignored -> updatePlayers();
            default -> {}
        }
    }
}
