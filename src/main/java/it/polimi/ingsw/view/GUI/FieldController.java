package it.polimi.ingsw.view.GUI;


import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.s2c.CardPlacedOnFieldMessage;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class FieldController implements ViewObserver {
    private View view;

    @FXML
    private GridPane fieldGrid;

    public void setView(View view) {
        this.view = view;
        view.addObserver(this);
        Platform.runLater(()->{
            fieldGrid.add(new CardBuilder(view.getMyField().getMatrix().get(new Coords(0,0))).getCardImage(), 4, 4);
        });
    }


    @Override
    public void update(NetMessage message) throws IOException {
        switch (message) {
            case CardPlacedOnFieldMessage m -> {
                if (m.getCard() instanceof StarterCard) return;
                int x = 4 + m.getCoords().getX() - m.getCoords().getY();
                int y = 4 + m.getCoords().getX() + m.getCoords().getY();
                Platform.runLater(()->{
                   fieldGrid.add(new CardBuilder(m.getCard()).getCardImage(), x, y);
                });
            }
            default -> {}
        }
    }
}
