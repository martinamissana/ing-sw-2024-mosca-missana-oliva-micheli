package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.network.RMI.RemoteInterface;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.view.RMIView;
import it.polimi.ingsw.view.TCPView;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewObserver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LoginScreenController implements ViewObserver {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private Stage stage;
    @FXML
    private TextField nickname;
    @FXML
    private TextField nickError;
    @FXML
    private Button login;
    @FXML
    private Button minimize;
    @FXML
    private ToggleButton fullscreen;
    @FXML
    private Button quit;

    private boolean isRMI;

    private View view;

    public LoginScreenController() {}


    public void minimize(ActionEvent event) {
        stage = (Stage) minimize.getScene().getWindow();
        stage.setIconified(true);
    }

    public void fullscreenToggle() {
        stage = (Stage) fullscreen.getScene().getWindow();
        stage.setFullScreen(!stage.isFullScreen());
    }

    public void closeWindow() {
        stage = (Stage) quit.getScene().getWindow();
        stage.close();
    }

    public void updateNicknameField() {

        //character length check
        if (nickname.getText().length() > 30) {
            nickname.setText(nickname.getText().substring(0, 30));
            nickname.positionCaret(nickname.getText().length());
            nickError.setText("character limit reached");
        } else {
            nickError.setText("");
        }

        // dynamic field size
        nickname.setPrefWidth(nickname.getText().length() * 7.1 + 17);

        // dynamic text alignment
        if (nickname.getText().length() * 7.1 + 20 >= nickname.getMinWidth()) nickname.setAlignment(Pos.CENTER_RIGHT);
        else nickname.setAlignment(Pos.CENTER);

        // check if enter
        nickname.setOnKeyReleased(event -> { if (event.getCode() == KeyCode.ENTER) { login.fire(); }});
    }


    public void goToMainMenu(ActionEvent actionEvent) {

        Stage stage = (Stage) (login.getScene().getWindow());
        String nick = nickname.getText().strip();

        // string validity + form check
        nickError.setText("");
        if (nick == null || nick.equals("")) {
            nickError.setText("invalid nickname");
            return;
        } else {
            nickError.setText("logging in...");
        }

        // create view
        try {
            if (isRMI) {
                Registry registry = null;
                registry = LocateRegistry.getRegistry();
                String remoteObjectName = "RMIServer";
                RemoteInterface RMIServer = null;
                try {
                    RMIServer = (RemoteInterface) registry.lookup(remoteObjectName);
                } catch (RemoteException | NotBoundException ex) {
                    throw new RuntimeException(ex);
                }
                view = new RMIView(RMIServer);
                } else {
                    view = new TCPView("127.0.0.1", 4321);
                    new Thread(() -> {
                    try {
                        ((TCPView) view).startClient();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }

        } catch (IOException | NotBoundException e) {
            throw new RuntimeException(e);
        }

        // login
        try {
            view.login(nick);
        } catch (NicknameAlreadyTakenException | IOException | FullLobbyException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.view.addObserver(this);

        // go to main menu
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/MainMenuScreen.fxml")); // not implemented yet
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, WIDTH, HEIGHT);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void update(NetMessage m) throws IOException {

    }
}