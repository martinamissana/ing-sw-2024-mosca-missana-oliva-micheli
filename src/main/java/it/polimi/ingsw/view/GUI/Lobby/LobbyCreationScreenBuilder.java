package it.polimi.ingsw.view.GUI.Lobby;

import it.polimi.ingsw.controller.exceptions.CannotJoinMultipleLobbiesException;
import it.polimi.ingsw.controller.exceptions.UnexistentUserException;
import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.LobbyDoesNotExistsException;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.view.GUI.ViewSingleton;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.io.IOException;

public class LobbyCreationScreenBuilder implements Builder<Region> {

    private final View view = ViewSingleton.getInstance().getView();
    private final ViewController viewCon = ViewSingleton.getInstance().getViewController();

    private final Runnable goBack;

    @FXML
    private Button backButton;
    @FXML
    private Spinner<Integer> numOfPlayersSpinner;
    @FXML
    private Button createLobbyButton;

    public LobbyCreationScreenBuilder(Runnable goBack) { this.goBack = goBack; }

    @Override
    public Region build() {

        Region lobbyCreationScreen;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LobbyCreationScreen.fxml"));
            loader.setController(this);
            lobbyCreationScreen = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        backButton.setOnAction(actionEvent -> goBack.run());
        numOfPlayersSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2,4,2));
        numOfPlayersSpinner.requestFocus();
        createLobbyButton.setOnAction(actionEvent -> {
            try {
                viewCon.checkCreateLobby(numOfPlayersSpinner.getValue());
                view.createLobby(numOfPlayersSpinner.getValue());
            } catch (LobbyDoesNotExistsException | UnexistentUserException | CannotJoinMultipleLobbiesException |
                     ClassNotFoundException | NicknameAlreadyTakenException | FullLobbyException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        return lobbyCreationScreen;
    }
}
