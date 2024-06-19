package it.polimi.ingsw.view.GUIFX;

import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.view.ViewObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class GameScreenController implements ViewObserver {

    private final ViewSingleton viewSingleton = ViewSingleton.getInstance();

    @FXML
    private Pane scoreboard;
    @FXML
    private Pane goals;
    @FXML
    private Pane decks;
    @FXML
    private Pane field;
    @FXML
    private Button chat;
    @FXML
    private Pane hand;

    public void initialize() {
        viewSingleton.getView().addObserver(this);
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Scoreboard.fxml"));
            Pane center = null;
            try {
                center = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            scoreboard.getChildren().add(center);
            ScoreboardController controller = loader.getController();
            controller.setScoreboard(viewSingleton.getView().getLobbies().get(viewSingleton.getView().getID()));


            FXMLLoader decksLoader = new FXMLLoader(getClass().getResource("/fxml/Decks.fxml"));
            Pane decksCenter = null;
            try {
                decksCenter = decksLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            decks.getChildren().add(decksCenter);
            DecksController decksController = decksLoader.getController();
            decksController.setView(viewSingleton.getView());


            FXMLLoader goalsLoader = new FXMLLoader(getClass().getResource("/fxml/Goals.fxml"));
            Pane goalsCenter = null;
            try {
                goalsCenter = goalsLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            goals.getChildren().add(goalsCenter);
            GoalsController goalsController = goalsLoader.getController();
            goalsController.setView(viewSingleton.getView());

            FXMLLoader handLoader = new FXMLLoader(getClass().getResource("/fxml/Hand.fxml"));
            Pane handCenter = null;
            try {
                handCenter = handLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            hand.getChildren().add(handCenter);
            HandController handController = handLoader.getController();
            handController.setView(viewSingleton.getView());


            FXMLLoader fieldLoader = new FXMLLoader(getClass().getResource("/fxml/Field.fxml"));
            Pane fieldCenter = null;
            try {
                fieldCenter = fieldLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            field.getChildren().add(fieldCenter);
            FieldController fieldController = fieldLoader.getController();
            fieldController.setView(viewSingleton.getView());
        });
    }

    @Override
    public void update(NetMessage m) throws IOException {

    }
}
