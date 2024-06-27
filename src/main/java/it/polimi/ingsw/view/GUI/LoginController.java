package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.DisconnectMessage;
import it.polimi.ingsw.network.netMessage.s2c.CurrentStatusMessage;
import it.polimi.ingsw.network.netMessage.s2c.LoginFail_NicknameAlreadyTaken;
import it.polimi.ingsw.network.netMessage.s2c.LoginSuccessMessage;
import it.polimi.ingsw.view.ViewObserver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import static java.lang.System.exit;

/**
 * Class LoginController
 * allows the player to login
 */
public class LoginController implements ViewObserver {
    @FXML
    private TextField nicknameField;
    @FXML
    private Label statusMessage;
    @FXML
    private MenuButton connectionMenu;
    @FXML
    private MenuItem TCP;
    @FXML
    private MenuItem RMI;
    @FXML
    private Pane connectionPane;
    @FXML
    private TextField IP;
    @FXML
    private StackPane infoIP;
    @FXML
    private TextField port;
    @FXML
    private StackPane infoPort;
    @FXML
    private Label connectionError;

    private final ViewSingleton viewSing = ViewSingleton.getInstance();
    private final Semaphore sem = new Semaphore(0);
    private MainLayout mainLayout;

    /**
     * creates info tooltips
     */
    public void initialize() {
        Tooltip tooltipIP = new Tooltip("If empty: Connection to local server");
        Tooltip.install(infoIP, tooltipIP);
        tooltipIP.setShowDelay(new Duration(0.2));

        Tooltip tooltipPort = new Tooltip("If empty: 4321");
        Tooltip.install(infoPort, tooltipPort);
        tooltipPort.setShowDelay(new Duration(0.2));
    }

    /**
     * @param mainLayout the main stage, used to switch the scene after login
     */
    public void setMainLayout(MainLayout mainLayout) {
        this.mainLayout = mainLayout;
    }

    @FXML
    public void modifyTags(KeyEvent keyEvent) {
        connectionError.setText("");

        // character length check
        if (nicknameField.getText().length() > 16) {
            nicknameField.setText(nicknameField.getText().substring(0, 16));
            nicknameField.positionCaret(nicknameField.getText().length());

            statusMessage.setText("character limit reached");
        } else statusMessage.setText("");

        if (keyEvent.getCode() == KeyCode.ENTER) login();
    }

    /**
     * handle the login button
     */
    @FXML
    public void login() {
        statusMessage.setText("");
        nicknameField.setText(nicknameField.getText().strip());

        // string validity + form check
        if (nicknameField.getText().isEmpty()) nicknameField.setText("Anto");
        if (port.getText().isEmpty()) port.setText("4321");

        // create view
        try {
            viewSing.initialize(connectionMenu.getText(), IP.getText(), port.getText());
        } catch (Exception e) {
            nicknameField.setText("");
            IP.setText("");
            port.setText("");
            connectionError.setText("Wrong values inserted");
            return;
        }
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
            case LoginSuccessMessage m -> {
                if (m.getNickname().equals(nicknameField.getText())) sem.release();
            }
            case LoginFail_NicknameAlreadyTaken ignored -> exit(1);
            case DisconnectMessage ignored -> exit(1);
            case CurrentStatusMessage ignored -> sem.release();
            default -> {}
        }
    }

    /**
     * allows the player to choose the connection type [RMI-TCP]
     * @param actionEvent connection type selected
     */
    public void chooseConnection(ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(TCP)) {
            connectionMenu.setText(TCP.getText());
            connectionPane.setVisible(true);
        }
        else {
            connectionMenu.setText(RMI.getText());
            connectionPane.setVisible(false);
        }
    }
}
