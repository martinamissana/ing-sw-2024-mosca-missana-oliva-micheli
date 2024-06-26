package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.controller.exceptions.CannotJoinMultipleLobbiesException;
import it.polimi.ingsw.controller.exceptions.UnexistentUserException;
import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.LobbyDoesNotExistException;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.LobbyJoinedMessage;
import it.polimi.ingsw.view.ViewObserver;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

public class LobbyInstanceController implements ViewObserver, Initializable {
    @FXML
    private Label players;
    @FXML
    private TextField idLobby;
    @FXML
    private Button joinButton;

    private Lobby lobby;
    private final ViewSingleton viewSingleton = ViewSingleton.getInstance();
    private final Semaphore sem = new Semaphore(0);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewSingleton.getView().addObserver(this);
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;

        idLobby.setText("LOBBY #" + lobby.getID());
        players.setText(lobby.getPlayers().size() + " / " + lobby.getNumOfPlayers());
    }

    @FXML
    public void joinLobby() {
        joinButton.fire();
        try {
            viewSingleton.getViewController().checkJoinLobby(lobby.getID());
            viewSingleton.getView().joinLobby(lobby.getID());
            sem.acquire();
            viewSingleton.getView().removeObserver(this);

        } catch (LobbyDoesNotExistException | CannotJoinMultipleLobbiesException | FullLobbyException ignored) {}
        catch (IOException | ClassNotFoundException | UnexistentUserException | NicknameAlreadyTakenException |
                 InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(NetMessage message) throws IOException {
        if (message instanceof LobbyJoinedMessage) sem.release();
    }
}
