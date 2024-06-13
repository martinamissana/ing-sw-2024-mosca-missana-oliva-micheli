package it.polimi.ingsw.view.GUIFX;

import it.polimi.ingsw.view.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class LoginController {
    @FXML
    TextField nicknameField;
    @FXML
    MenuButton connectionMenu;
    @FXML
    MenuItem TCP;
    @FXML
    MenuItem RMI;
    @FXML
    Label statusMessage;

    View view;

    @FXML
    public void menuOptions(MouseEvent mouseEvent) {
        connectionMenu.fire();
    }

    @FXML
    public void modifyNickname(KeyEvent keyEvent) {

    }

    @FXML
    public void login(MouseEvent mouseEvent) {
        statusMessage.setText("");
        nicknameField.setText(nicknameField.getText().strip());

        // string validity + form check
        if (nicknameField.getText().isEmpty()) nicknameField.setText("Anto");
        statusMessage.setText("Logging in...");

    }

    public void chooseConnection(ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(TCP)) connectionMenu.setText(TCP.getText());
        else connectionMenu.setText(RMI.getText());
    }
}
