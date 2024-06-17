package it.polimi.ingsw.view.GUIFX;

import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.LobbyJoinedMessage;
import it.polimi.ingsw.network.netMessage.s2c.GameCreatedMessage;
import it.polimi.ingsw.network.netMessage.s2c.LobbyCreatedMessage;
import it.polimi.ingsw.network.netMessage.s2c.LobbyLeftMessage;
import it.polimi.ingsw.view.ViewObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainLayout implements ViewObserver {
    @FXML
    BorderPane layout;
    Stage stage;
    ViewSingleton viewSingleton;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setScene(String fxmlPath) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlPath + ".fxml"));
                AnchorPane center = loader.load();
                layout.setCenter(center);
                if (fxmlPath.equals("Login")) {
                    LoginController controller = loader.getController();
                    controller.setMainLayout(this);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void setStyle() {
        stage.initStyle(StageStyle.UNDECORATED);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TopBar.fxml"));
        try {
            layout.setTop(loader.load());
            TopBarController controller = loader.getController();
            controller.setStage(stage);
            controller.setMainLayout(this);

            Image bgImage = new Image(String.valueOf(getClass().getResource("/fxml/bg.png")));
            Background background = new Background(new BackgroundImage(bgImage, null, null, null, null));
            layout.setBackground(background);

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
            }
            case GameCreatedMessage m -> {
                layout.setBackground(null);
                // if (m.getID().equals(viewSingleton.getView().getID())) setScene("GameScreen");
            }
            default -> System.out.println(message.getClass());
        }
    }
}
