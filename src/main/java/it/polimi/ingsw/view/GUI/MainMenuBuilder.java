package it.polimi.ingsw.view.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.io.IOException;

public class MainMenuBuilder implements Builder<Region> {

    private final Runnable goLobbyCreation;
    private final Runnable goToExistingLobbies;

    @FXML
    private Button newLobbyButton;
    @FXML
    private Button joinLobbyButton;

    public MainMenuBuilder(Runnable goToLobbyCreation, Runnable goToExistingLobbies) {
        this.goLobbyCreation = goToLobbyCreation;
        this.goToExistingLobbies = goToExistingLobbies;
    }

    @Override
    public Region build() {
        Region mainMenu;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            loader.setController(this);
            mainMenu = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        newLobbyButton.setOnAction(actionEvent -> goLobbyCreation.run());
        joinLobbyButton.setOnAction(actionEvent -> goToExistingLobbies.run());

        return mainMenu;
    }
}
