package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.DisconnectMessage;
import it.polimi.ingsw.network.netMessage.s2c.CurrentStatusMessage;
import it.polimi.ingsw.network.netMessage.s2c.LoginFail_NicknameAlreadyTaken;
import it.polimi.ingsw.network.netMessage.s2c.LoginMessage;
import it.polimi.ingsw.view.ViewObserver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import static java.lang.System.exit;

public class LoginController implements ViewObserver {
    @FXML
    TextField nicknameField;
    @FXML
    MenuButton connectionMenu;
    @FXML
    Button loginButton;
    @FXML
    MenuItem TCP;
    @FXML
    MenuItem RMI;
    @FXML
    Label statusMessage;

    ViewSingleton viewSing = ViewSingleton.getInstance();
    Semaphore sem = new Semaphore(0);
    MainLayout mainLayout;

    public void setMainLayout(MainLayout mainLayout) {
        this.mainLayout = mainLayout;
    }

    @FXML
    public void menuOptions(MouseEvent mouseEvent) {
        connectionMenu.fire();
    }

    @FXML
    public void modifyNickname(KeyEvent keyEvent) {
        // character length check
        if (nicknameField.getText().length() > 16) {
            nicknameField.setText(nicknameField.getText().substring(0, 16));
            nicknameField.positionCaret(nicknameField.getText().length());

            statusMessage.setText("character limit reached");
        } else statusMessage.setText("");

        if (keyEvent.getCode() == KeyCode.ENTER) loginButton.fire();
    }

    @FXML
    public void login(MouseEvent mouseEvent) {
        statusMessage.setText("");
        nicknameField.setText(nicknameField.getText().strip());

        // string validity + form check
        if (nicknameField.getText().isEmpty()) nicknameField.setText("Anto");
        statusMessage.setText("Logging in...");

        // create view
        viewSing.initialize(connectionMenu.getText());
        viewSing.getView().addObserver(this);

        // login + get current lobby status
        try {
            viewSing.getView().login(nicknameField.getText());
            sem.acquire();
            viewSing.getView().getCurrentStatus();
            sem.acquire();
            viewSing.getView().removeObserver(this);
            viewSing.getView().addObserver(mainLayout);
            mainLayout.viewSingleton = this.viewSing;

            //Creation of scene
            mainLayout.setScene("Open");

        } catch (NicknameAlreadyTakenException | IOException | FullLobbyException | ClassNotFoundException e) {
            statusMessage.setText("Nickname already taken");
            viewSing.getView().removeObserver(this);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(NetMessage message) throws IOException {
        switch (message) {
            case LoginMessage m -> {
                if (m.getNickname().equals(nicknameField.getText())) sem.release();
            }
            case LoginFail_NicknameAlreadyTaken ignored -> exit(1);
            case DisconnectMessage ignored -> exit(1);
            case CurrentStatusMessage ignored -> sem.release();
            default -> {}
        }
    }

    public void chooseConnection(ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(TCP)) connectionMenu.setText(TCP.getText());
        else connectionMenu.setText(RMI.getText());
    }
}
