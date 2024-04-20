module PSP001 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.google.gson;

    opens it.polimi.ingsw.model.card;
    opens it.polimi.ingsw.model.commonItem;
    opens it.polimi.ingsw.model.goal;
}