package it.polimi.ingsw.view.GUIFX;

import it.polimi.ingsw.controller.exceptions.CannotJoinMultipleLobbiesException;
import it.polimi.ingsw.controller.exceptions.UnexistentUserException;
import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.LobbyDoesNotExistsException;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.LobbyJoinedMessage;
import it.polimi.ingsw.network.netMessage.s2c.LobbyCreatedMessage;
import it.polimi.ingsw.view.ViewObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

public class LobbyInstanceController implements ViewObserver, Initializable {
    @FXML
    Label players;
    @FXML
    TextField idLobby;
    @FXML
    Button joinButton;

    private Lobby lobby;
    private final ViewSingleton viewSingleton = ViewSingleton.getInstance();
    private final Semaphore sem = new Semaphore(0);

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;

        idLobby.setText("LOBBY #" + lobby.getID());
        players.setText(lobby.getPlayers().size() + " / " + lobby.getNumOfPlayers());
    }

    public void joinLobby(MouseEvent mouseEvent) {
        joinButton.fire();
        try {
            viewSingleton.getViewController().checkJoinLobby(lobby.getID());
            viewSingleton.getView().joinLobby(lobby.getID());
            sem.acquire();
            viewSingleton.getView().removeObserver(this);

            Parent root = FXMLLoader.load(getClass().getResource("/fxml/InLobby.fxml"));
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (LobbyDoesNotExistsException ignored) {}
        catch (FullLobbyException e) {
            System.out.println("Full lobby");
        } catch (CannotJoinMultipleLobbiesException e) {
            System.out.println("Multiple lobbies");
        } catch (IOException | ClassNotFoundException | UnexistentUserException | NicknameAlreadyTakenException |
                 InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(NetMessage message) throws IOException {
        if (message instanceof LobbyJoinedMessage) sem.release();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewSingleton.getView().addObserver(this);
    }
}
