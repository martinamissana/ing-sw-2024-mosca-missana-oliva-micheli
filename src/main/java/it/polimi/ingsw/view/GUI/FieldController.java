package it.polimi.ingsw.view.GUI;



import it.polimi.ingsw.controller.exceptions.IllegalActionException;
import it.polimi.ingsw.controller.exceptions.NotYourTurnException;
import it.polimi.ingsw.controller.exceptions.UnexistentUserException;
import it.polimi.ingsw.model.card.CardBlock;
import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.model.exceptions.GameDoesNotExistException;
import it.polimi.ingsw.model.exceptions.IllegalCoordsException;
import it.polimi.ingsw.model.exceptions.IllegalMoveException;
import it.polimi.ingsw.model.exceptions.LobbyDoesNotExistsException;
import it.polimi.ingsw.model.game.Action;
import it.polimi.ingsw.model.game.GamePhase;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.s2c.CardPlacedOnFieldMessage;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewController;
import it.polimi.ingsw.view.ViewObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class FieldController implements ViewObserver {
    private View view;
    private HandController hand;
    private ViewController check;

    @FXML
    private GridPane fieldGrid;


    private Button createButton(){
        Button button = new Button();
        button.setStyle("-fx-background-radius: 5; -fx-background-color: #F2DEBD;");
        button.setPrefWidth(100);
        button.setPrefHeight(66.66);
        button.setVisible(true);
        button.setOpacity(0.5);
        return button;
    }

    public void setView(View view, ViewController  check) {
        this.view = view;
        this.check = check;
        view.addObserver(this);
        Platform.runLater(()->{
            try {
                view.getMyField().checkIfPlaceable(new Coords(0,1));
                Button button = createButton();
                button.setOnAction(event -> {
                    playCard(0, 1);
                });
                fieldGrid.add(button,3,3);
            } catch (IllegalCoordsException ignored) {}
            try {
                view.getMyField().checkIfPlaceable(new Coords(0,-1));
                Button button = createButton();
                button.setOnAction(event -> {
                    playCard(0, -1);
                });
                fieldGrid.add(button,5,5);
            } catch (IllegalCoordsException ignored) {
            }
            try {
                view.getMyField().checkIfPlaceable(new Coords(1,0));
                Button button = createButton();
                button.setOnAction(event -> {
                    playCard(1, 0);
                });
                fieldGrid.add(button,5,3);
            } catch (IllegalCoordsException ignored) {
            }
            try {
                view.getMyField().checkIfPlaceable(new Coords(-1, 0));
                Button button = createButton();
                button.setOnAction(event -> {
                    playCard(-1, 0);
                });
                fieldGrid.add(button, 3, 5);
            } catch (IllegalCoordsException ignored) {
            }
            fieldGrid.add(new CardBuilder(view.getMyField().getMatrix().get(new Coords(0,0))).getCardImage(), 4, 4);
        });
    }

    public void setHand(HandController hand) {
        this.hand = hand;
    }

    public void playCard(int x,int y){
        if(!view.isYourTurn()||!view.getGamePhase().equals(GamePhase.PLAYING_GAME)||!view.getAction().equals(Action.PLAY)) return;
        try {
            view.playCard(hand.getCardPlacedPos(),new Coords(x,y),view.getHand().getCard(hand.getCardPlacedPos()).getSide());
        } catch (IllegalActionException | NotYourTurnException | IllegalMoveException | GameDoesNotExistException |
                 LobbyDoesNotExistsException | UnexistentUserException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(NetMessage message) throws IOException {
        switch (message) {
            case CardPlacedOnFieldMessage m -> {
                if (m.getCard() instanceof StarterCard) return;
                if(m.getNickname().equals(view.getNickname())) {
                    int x = 4 + m.getCoords().getX() - m.getCoords().getY();
                    int y = 4 - m.getCoords().getX() - m.getCoords().getY();
                    Platform.runLater(() -> {
                        try {
                            view.getMyField().checkIfPlaceable(new Coords(m.getCoords().getX(), m.getCoords().getY() + 1));
                            Button button = createButton();
                            button.setOnAction(event -> {
                                playCard(m.getCoords().getX(), m.getCoords().getY() + 1);
                            });
                            fieldGrid.add(button, x - 1, y - 1);
                        } catch (IllegalCoordsException ignored) {
                            if(view.getMyField().getMatrix().get(new Coords(m.getCoords().getX(), m.getCoords().getY() + 1)).getClass() == CardBlock.class)
                                fieldGrid.getChildren().remove(getNodeByRowColumnIndex(x-1,y-1,fieldGrid));
                        }

                        try {
                            view.getMyField().checkIfPlaceable(new Coords(m.getCoords().getX(), m.getCoords().getY() - 1));
                            Button button = createButton();
                            button.setOnAction(event -> {
                                playCard(m.getCoords().getX(), m.getCoords().getY() - 1);
                            });
                            fieldGrid.add(button, x + 1, y + 1);
                        } catch (IllegalCoordsException ignored) {
                            if(view.getMyField().getMatrix().get(new Coords(m.getCoords().getX(), m.getCoords().getY() - 1)).getClass() == CardBlock.class)
                                fieldGrid.getChildren().remove(getNodeByRowColumnIndex(x+1,y+1,fieldGrid));
                        }
                        try {
                            view.getMyField().checkIfPlaceable(new Coords(m.getCoords().getX() + 1, m.getCoords().getY()));
                            Button button = createButton();
                            button.setOnAction(event -> {
                                playCard(m.getCoords().getX() + 1, m.getCoords().getY());
                            });
                            fieldGrid.add(button, x + 1, y - 1);
                        } catch (IllegalCoordsException ignored) {
                            if(view.getMyField().getMatrix().get(new Coords(m.getCoords().getX() + 1, m.getCoords().getY())).getClass() == CardBlock.class)
                                fieldGrid.getChildren().remove(getNodeByRowColumnIndex(x+1,y-1,fieldGrid));
                        }
                        try {
                            view.getMyField().checkIfPlaceable(new Coords(m.getCoords().getX() - 1, m.getCoords().getY()));
                            Button button = createButton();
                            button.setOnAction(event -> {
                                playCard(m.getCoords().getX() - 1, m.getCoords().getY());
                            });
                            fieldGrid.add(button, x - 1, y + 1);
                        } catch (IllegalCoordsException ignored) {
                            if(view.getMyField().getMatrix().get(new Coords(m.getCoords().getX()-1, m.getCoords().getY())).getClass() == CardBlock.class)
                                fieldGrid.getChildren().remove(getNodeByRowColumnIndex(x-1,y+1,fieldGrid));
                        }
                        fieldGrid.add(new CardBuilder(m.getCard()).getCardImage(), x, y);
                    });
                }
            }
            default -> {}
        }
    }

    private Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                return node;
            }
        }
        return null;
    }


}

