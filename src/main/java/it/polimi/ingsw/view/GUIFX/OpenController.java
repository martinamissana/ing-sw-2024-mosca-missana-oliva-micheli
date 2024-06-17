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
import it.polimi.ingsw.network.netMessage.s2c.LobbyDeletedMessage;
import it.polimi.ingsw.network.netMessage.s2c.LobbyLeftMessage;
import it.polimi.ingsw.network.netMessage.s2c.PawnAssignedMessage;
import it.polimi.ingsw.view.ViewObserver;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

public class OpenController implements ViewObserver, Initializable {
    @FXML
    MenuButton playersNumber;
    @FXML
    MenuItem two;
    @FXML
    MenuItem three;
    @FXML
    MenuItem four;

    @FXML
    ListView<Pane> lobbies;
    @FXML
    Button lobbyCreation;
    @FXML
    Label helloLabel;

    private final ViewSingleton viewSingleton = ViewSingleton.getInstance();
    Semaphore sem = new Semaphore(0);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewSingleton.getView().addObserver(this);
        helloLabel.setText("Hello " + viewSingleton.getView().getNickname() + "!!");
        updateLobbies();
    }

    public void updateLobbies() {       // TODO: Fix bug
        lobbies.getItems().removeAll();
        HashMap<Integer, Lobby> lobbiesMap = viewSingleton.getView().getLobbies();

        for (Integer i : lobbiesMap.keySet()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LobbyInstance.fxml"));
                AnchorPane lobbyPane = loader.load();
                LobbyInstanceController controller = loader.getController();
                controller.setLobby(lobbiesMap.get(i));

                lobbies.getItems().add(lobbyPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void choosePlayers(ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(two)) playersNumber.setText(two.getText());
        else if (actionEvent.getSource().equals(three)) playersNumber.setText(three.getText());
        else playersNumber.setText(four.getText());
    }

    public void createLobby(MouseEvent mouseEvent) {
        try {
            viewSingleton.getViewController().checkCreateLobby(Integer.parseInt(playersNumber.getText()));
            viewSingleton.getView().createLobby(Integer.parseInt(playersNumber.getText()));
            sem.acquire();

        } catch (CannotJoinMultipleLobbiesException e) {
            System.out.println("Multiple lobbies");
        } catch (FullLobbyException | LobbyDoesNotExistsException | NicknameAlreadyTakenException ignored) {}
        catch (IOException | ClassNotFoundException | UnexistentUserException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void menuOptions(MouseEvent ignored) {
        playersNumber.fire();
    }

    @Override
    public void update(NetMessage message) throws IOException {
        switch (message) {
            case LobbyCreatedMessage ignored -> {
                if (viewSingleton.getView().getID() == null) updateLobbies();
                else sem.release();
            }
            case LobbyJoinedMessage ignored -> {
                if (viewSingleton.getView().getID() == null) updateLobbies();
            }
            case LobbyLeftMessage ignored -> updateLobbies();
            case LobbyDeletedMessage ignored -> updateLobbies();

            default -> {}
        }
    }
}
