package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.network.RMI.RemoteInterface;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.s2c.LoginFail_NicknameAlreadyTaken;
import it.polimi.ingsw.network.netMessage.s2c.LoginMessage;
import it.polimi.ingsw.view.RMIView;
import it.polimi.ingsw.view.TCPView;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Path;
import javafx.util.Builder;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LoginScreenBuilder implements Builder<Node>, ViewObserver {

    private final LoginInformation loginInformation = new LoginInformation();
    private final Runnable screenSwapper;

    @FXML
    private TextField nicknameField;
    @FXML
    private TextField displayText;
    @FXML
    private Button loginButton;
    @FXML
    private HBox connectionButton;
    @FXML
    private Path tcpShape;
    @FXML
    private Path rmiShape;

    public LoginScreenBuilder(Runnable screenSwapper) {
        this.screenSwapper = screenSwapper;
    }

    @Override
    public Region build() {
        Region loginScreen;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginScreen.fxml"));
            loader.setController(this);
            loginScreen = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // bind login information proprieties
        nicknameField.textProperty().bindBidirectional(loginInformation.nicknameProperty());
        nicknameField.setText("");

        nicknameField.setOnKeyTyped(keyEvent -> {

            // character length check
            if (nicknameField.getText().length() > 30) {
                nicknameField.setText(nicknameField.getText().substring(0, 30));
                nicknameField.positionCaret(nicknameField.getText().length());
                displayText.setText("character limit reached");
            } else
                displayText.setText("");

            // dynamic field size
            nicknameField.setPrefWidth(nicknameField.getText().length() * 7.1 + 17);

            // dynamic text alignment
            if (nicknameField.getText().length() * 7.1 + 20 >= nicknameField.getMinWidth())
                nicknameField.setAlignment(Pos.CENTER_RIGHT);
            else
                nicknameField.setAlignment(Pos.CENTER);

            // if key pressed is 'enter', fire the login button
            if (keyEvent.getCode() == KeyCode.ENTER) loginButton.fire();
        });

        connectionButton.setOnKeyPressed(keyEvent -> {

            if (keyEvent.getCode() == KeyCode.SPACE) {
                loginInformation.isRMIProperty().set(!loginInformation.isRMIProperty().get());
                if (loginInformation.isRMIProperty().get()) {
                    tcpShape.setFill(Paint.valueOf("#FFFFFF"));
                    rmiShape.setFill(Paint.valueOf("#72b043"));
                } else {
                    tcpShape.setFill(Paint.valueOf("#72b043"));
                    rmiShape.setFill(Paint.valueOf("#FFFFFF"));
                }
            }
        });
        tcpShape.setFill(Paint.valueOf("#72b043"));

        loginButton.setOnAction(actionEvent -> {
            displayText.setText("");

            nicknameField.setText(nicknameField.getText().strip());

            // string validity + form check
            if (nicknameField.getText().isEmpty()) {
                displayText.setText("invalid nickname");
                return;
            } else
                displayText.setText("logging in...");

            // create view according to isRMI
            ViewSingleton viewSing = ViewSingleton.getInstance();
            try {
                viewSing.initialize(loginInformation.isRMIProperty().get());
            } catch (NotBoundException | IOException e) {
                throw new RuntimeException(e);
            }

            viewSing.getView().addObserver(this);

            // login
            try {
                viewSing.getView().login(nicknameField.getText());
            } catch (NicknameAlreadyTakenException | IOException | FullLobbyException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        return loginScreen;
    }

    @Override
    public void update(NetMessage message) throws IOException {
        switch (message) {
            case LoginMessage m -> {
                if (m.getNickname().equals(nicknameField.getText()))
                    displayText.setText("login successful");
                Platform.runLater(screenSwapper);
            }
            case LoginFail_NicknameAlreadyTaken ignored -> {
                displayText.setText("nickname already taken");

            }
            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
    }
}