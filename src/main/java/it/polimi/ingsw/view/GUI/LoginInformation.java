package it.polimi.ingsw.view.GUI;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoginInformation {
    private final StringProperty nickname;
    private final BooleanProperty isRMI;

    public LoginInformation() {
        nickname = new SimpleStringProperty();
        isRMI = new SimpleBooleanProperty();
    }

    public StringProperty nicknameProperty() { return nickname; }
    public BooleanProperty isRMIProperty() { return isRMI; }

    public void setIsRMI(boolean value) { this.isRMI.set(value); }
}
