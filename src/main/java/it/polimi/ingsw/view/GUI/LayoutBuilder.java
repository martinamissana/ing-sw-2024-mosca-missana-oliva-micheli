package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewObserver;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Builder;

import java.io.IOException;

public class LayoutBuilder implements Builder<Node>, ViewObserver {
    final Node topBar;
    Node loginScreen;
    Node mainMenu = new MainMenuBuilder().build();
    // future screens will be put here

    private View view;

    public LayoutBuilder(Node topBar) {
        this.topBar = topBar;
    }

    @Override
    public Region build() {

        BorderPane mainPane = new BorderPane();
        mainPane.setBackground(Background.fill(Color.BLACK));
        mainPane.setPadding(Insets.EMPTY);

//        ImageView backgroundImage = new ImageView();
//        backgroundImage.setImage(new Image("/fxml/bg.png"));
//        mainPane.getChildren().add(backgroundImage);

        // set the top bar
        mainPane.setTop(topBar);

        // set the login screen to the center node
        loginScreen = new LoginScreenBuilder(() -> mainPane.setCenter(mainMenu)).build();
        mainPane.setCenter(loginScreen);
        return mainPane;
    }

    public void setView(View view) { this.view = view; }

    @Override
    public void update(NetMessage m) throws IOException {

    }
}