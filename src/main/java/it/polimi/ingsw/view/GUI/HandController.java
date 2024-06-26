package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.model.game.GamePhase;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.s2c.*;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;


public class HandController implements ViewObserver {
    private View view;
    private Integer cardPlacedPos = null;

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

    public void setView(View view) {
        this.view = view;
        view.addObserver(this);

        flip0.setVisible(false);
        flip1.setVisible(false);
        flip2.setVisible(false);
    }

    @Override
    public void update(NetMessage message) throws IOException {
        switch (message) {
            case CardPlacedOnFieldMessage m -> {
                if (m.getNickname().equals(view.getNickname())) {
                    if (!(m.getCard() instanceof StarterCard)) {
                        Platform.runLater(() -> {
                            card0.getChildren().clear();
                            card1.getChildren().clear();
                            card2.getChildren().clear();
                            card0.getChildren().add(new CardBuilder(view.getHand().getCard(0)).getCardImage());
                            card1.getChildren().add(new CardBuilder(view.getHand().getCard(1)).getCardImage());
                            // TODO: See if problem is fixed at end of the game by removing hand from observers:
                            //  Exception in thread "JavaFX Application Thread" java.lang.IndexOutOfBoundsException: Index 1 out of bounds for length 0
                        });
                    }
                }
            }
            case CardDrawnFromSourceMessage ignored -> {
                if (view.getHand() != null && view.getHand().getSize() > 2) {
                    if (view.getHand().getCard(2) != null) {
                        if ((view.getHand().getCard(2).getSide().equals(CardSide.BACK)))
                            view.getHand().getCard(2).flip();
                        Platform.runLater(() -> card2.getChildren().add(new CardBuilder(view.getHand().getCard(2)).getCardImage()));
                    }
                }
            }
            case GamePhaseChangedMessage m -> {
                if (m.getGamePhase().equals(GamePhase.CHOOSING_SECRET_GOAL)) {
                    Platform.runLater(() -> {
                        for (int i = 0; i < 3; i++) {
                            if (view.getHand().getCard(i).getSide() == CardSide.BACK) {
                                view.getHand().getCard(i).flip();
                            }
                        }
                        card0.getChildren().add(new CardBuilder(view.getHand().getCard(0)).getCardImage());
                        card1.getChildren().add(new CardBuilder(view.getHand().getCard(1)).getCardImage());
                        card2.getChildren().add(new CardBuilder(view.getHand().getCard(2)).getCardImage());
                        flip0.setVisible(true);
                        flip1.setVisible(true);
                        flip2.setVisible(true);
                    });
                }
            }
            case LobbyLeftMessage ignored -> view.removeObserver(this);
            case GameWinnersAnnouncedMessage ignored -> view.removeObserver(this);
            default -> {
            }
        }
    }

    public int getCardPlacedPos() {
        return cardPlacedPos;
    }


    public void playCard(MouseEvent mouseEvent) {
        if (mouseEvent.getSource().equals(card0)) {
            cardPlacedPos = 0;
            return;
        }
        if (mouseEvent.getSource().equals(card1)) {
            cardPlacedPos = 1;
            return;
        }
        if (mouseEvent.getSource().equals(card2)) {
            cardPlacedPos = 2;
        }

    }

    public void flip(MouseEvent mouseEvent) {
        if (mouseEvent.getSource().equals(flip0)) {
            if (view.getHand().getSize() == 0 || view.getHand().getCard(0) instanceof StarterCard) return;
            view.getHand().getCard(0).flip();
            card0.getChildren().removeAll();
            card0.getChildren().add(new CardBuilder(view.getHand().getCard(0)).getCardImage());
            return;
        }
        if (mouseEvent.getSource().equals(flip1)) {
            if (view.getHand().getSize() < 2) return;
            view.getHand().getCard(1).flip();
            card1.getChildren().removeAll();
            card1.getChildren().add(new CardBuilder(view.getHand().getCard(1)).getCardImage());
            return;
        }
        if (mouseEvent.getSource().equals(flip2)) {
            if (view.getHand().getSize() < 3) return;
            view.getHand().getCard(2).flip();
            card2.getChildren().removeAll();
            card2.getChildren().add(new CardBuilder(view.getHand().getCard(2)).getCardImage());
        }
    }
}
