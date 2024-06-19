package it.polimi.ingsw.view.GUIFX;

import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.s2c.CardAddedToHandMessage;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewObserver;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;


public class HandController implements ViewObserver {
    private View view;
    private int cardPlacedPos;

    @FXML
    private Pane card0;
    @FXML
    private Pane card1;
    @FXML
    private Pane card2;
    @FXML
    private Button flip0;
    @FXML
    private Button flip1;
    @FXML
    private Button flip2;

    public void setView(View view){
        this.view = view;
        view.addObserver(this);
        card0.getChildren().add(new CardBuilder(view.getHand().getCard(0)).getCardImage());
    }

    @Override
    public void update(NetMessage message) throws IOException {
        switch (message){
            case CardAddedToHandMessage ignored -> {
                card0.getChildren().add(new CardBuilder(view.getHand().getCard(0)).getCardImage());
                card1.getChildren().add(new CardBuilder(view.getHand().getCard(1)).getCardImage());
                card2.getChildren().add(new CardBuilder(view.getHand().getCard(2)).getCardImage());
            }
            default -> {}
        }
    }

    public int getCardPlacedPos() {
        return cardPlacedPos;
    }

    public void removeCard(int cardPlacedPos){
        if(cardPlacedPos == 0){
            card0.getChildren().removeAll();
            return;
        }
        if(cardPlacedPos == 1){
            card1.getChildren().removeAll();
            return;
        }
        if(cardPlacedPos == 2){
            card2.getChildren().removeAll();
        }
    }

    public void playCard(MouseEvent mouseEvent) {
        if (mouseEvent.getSource().equals(card0)){
           cardPlacedPos = 0;
           return;
        }
        if (mouseEvent.getSource().equals(card1)){
            cardPlacedPos = 1;
            return;
        }
        if (mouseEvent.getSource().equals(card1)){
            cardPlacedPos = 2;
        }

    }

    public void flip(MouseEvent mouseEvent) {
        if(mouseEvent.getSource().equals(flip0)){
            if(view.getHand().getCard(0) == null) return;
            view.getHand().getCard(0).flip();
            card0.getChildren().removeAll();
            card0.getChildren().add(new CardBuilder( view.getHand().getCard(0)).getCardImage());
            return;
        }
        if(mouseEvent.getSource().equals(flip1)){
            if(view.getHand().getCard(1) == null) return;
            view.getHand().getCard(1).flip();
            card1.getChildren().removeAll();
            card1.getChildren().add(new CardBuilder( view.getHand().getCard(1)).getCardImage());
            return;
        }
        if(mouseEvent.getSource().equals(flip2)){
            if(view.getHand().getCard(2) == null) return;
            view.getHand().getCard(2).flip();
            card2.getChildren().removeAll();
            card2.getChildren().add(new CardBuilder( view.getHand().getCard(2)).getCardImage());
        }
    }
}
