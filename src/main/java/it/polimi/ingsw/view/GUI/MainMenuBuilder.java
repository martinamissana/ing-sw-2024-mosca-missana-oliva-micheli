package it.polimi.ingsw.view.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.io.IOException;

public class MainMenuBuilder implements Builder<Node> {

    @FXML
    private Button hiButton;

    public MainMenuBuilder() {}

    @Override
    public Node build() {
        Region mainMenu = new Region();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            loader.setController(this);
            mainMenu = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        hiButton.setOnAction(actionEvent -> System.out.println("hi button says hi"));

        return mainMenu;
    }
}
