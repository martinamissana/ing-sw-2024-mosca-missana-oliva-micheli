package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.controller.exceptions.IllegalActionException;
import it.polimi.ingsw.controller.exceptions.NotYourTurnException;
import it.polimi.ingsw.controller.exceptions.UnexistentUserException;
import it.polimi.ingsw.model.card.CardBlock;
import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.game.Action;
import it.polimi.ingsw.model.game.GamePhase;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.s2c.CardPlacedOnFieldMessage;
import it.polimi.ingsw.network.netMessage.s2c.FailMessage;
import it.polimi.ingsw.network.netMessage.s2c.GameWinnersAnnouncedMessage;
import it.polimi.ingsw.network.netMessage.s2c.LobbyLeftMessage;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;

public class FieldController implements ViewObserver {
    private View view;
    private HandController hand;

    @FXML
    private GridPane fieldGrid;
    @FXML
    private Label errorMessage;

    private int starterX = 4;
    private int starterY = 4;


    private Button createButton(){
        Button button = new Button();
        button.setStyle("-fx-background-radius: 5; -fx-background-color: #F2DEBD;");
        button.setPrefWidth(100);
        button.setPrefHeight(66.66);
        button.setVisible(true);
        button.setOpacity(0.5);
        return button;
    }

    public void setView(View view) {
        this.view = view;
        view.addObserver(this);
        Platform.runLater(()-> {
            for (Coords c : getNeighbors(new Coords(0, 0))) {
                try {
                    view.getMyField().checkIfPlaceable(c);
                    Button button = createButton();
                    button.setOnAction(event -> playCard(c.getX(), c.getY()));
                    fieldGrid.add(button, starterX + c.getX() - c.getY(), starterY - c.getX() - c.getY());
                } catch (IllegalCoordsException ignored) {}
            }

            fieldGrid.add(new CardBuilder(view.getMyField().getMatrix().get(new Coords(0,0))).getCardImage(), starterX, starterY);
        });
    }

    public void setHand(HandController hand) {
        this.hand = hand;
    }

    public void playCard(int x,int y) {
        if(!view.isYourTurn()||!view.getGamePhase().equals(GamePhase.PLAYING_GAME)||!view.getAction().equals(Action.PLAY)) return;
        try {
            view.playCard(hand.getCardPlacedPos(),new Coords(x,y),view.getHand().getCard(hand.getCardPlacedPos()).getSide());
        } catch (IllegalActionException | IllegalMoveException | NotYourTurnException ignored) {
        } catch (GameDoesNotExistException | LobbyDoesNotExistException | UnexistentUserException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void hideLabel() {
        errorMessage.setVisible(false);
    }

    @Override
    public void update(NetMessage message) throws IOException {
        switch (message) {
            case CardPlacedOnFieldMessage m -> {
                if (m.getCard() instanceof StarterCard) return;
                if (m.getNickname().equals(view.getNickname())) {
                    Platform.runLater(() -> {
                        int x = starterX + m.getCoords().getX() - m.getCoords().getY();
                        int y = starterY - m.getCoords().getX() - m.getCoords().getY();

                        if (x == 0) {
                            shiftColumns();
                            fieldGrid.addColumn(0);
                            starterX++;
                            x = 1;
                        }

                        if (y == 0) {
                            shiftRows();
                            fieldGrid.addRow(0);
                            starterY++;
                            y = 1;
                        }

                        for (Coords c : getNeighbors(m.getCoords())) {
                            try {
                                view.getMyField().checkIfPlaceable(c);
                                Button button = createButton();
                                button.setOnAction(event -> playCard(c.getX(), c.getY()));
                                if (getNodeByRowColumnIndex(starterX + c.getX() - c.getY(), starterY - c.getX() - c.getY()) == null) fieldGrid.add(button, starterX + c.getX() - c.getY(), starterY - c.getX() - c.getY());
                                // TODO: fix "Exception in thread "JavaFX Application Thread" java.lang.NullPointerException: Cannot invoke "Object.getClass()" because the return value of "java.util.HashMap.get(Object)" is null"
                            } catch (IllegalCoordsException e) {
                                if (view.getMyField().getMatrix().get(c) instanceof CardBlock && getNodeByRowColumnIndex(starterX + c.getX() - c.getY(), starterY - c.getX() - c.getY()) != null) {
                                    fieldGrid.getChildren().remove(getNodeByRowColumnIndex(starterX + c.getX() - c.getY(), starterY - c.getX() - c.getY()));
                                }
                            }
                        }

                        fieldGrid.getChildren().remove(getNodeByRowColumnIndex(x, y));
                        fieldGrid.add(new CardBuilder(m.getCard()).getCardImage(), x, y);
                    });
                }
            }
            case FailMessage m -> Platform.runLater(() -> {
                if (m.getMessage().equals("Requirements not satisfied to place the card")) {
                    errorMessage.setVisible(true);
                    errorMessage.setText("Cannot play card: Requirements not satisfied");
                }
            });
            case LobbyLeftMessage ignored -> view.removeObserver(this);
            case GameWinnersAnnouncedMessage ignored -> view.removeObserver(this);
            default -> {}
        }
    }

    private Node getNodeByRowColumnIndex(final int column, final int row) {
        for (Node node : fieldGrid.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                return node;
            }
        }
        return null;
    }

    private void shiftRows() {
        fieldGrid.getChildren().forEach(node -> {
            Integer row = GridPane.getRowIndex(node);
            if (row != null) {
                GridPane.setRowIndex(node, row + 1);
            }
        });
    }

    private void shiftColumns() {
        fieldGrid.getChildren().forEach(node -> {
            Integer column = GridPane.getColumnIndex(node);
            if (column != null) {
                GridPane.setColumnIndex(node, column + 1);
            }
        });
    }

    private ArrayList<Coords> getNeighbors(Coords coords) {
        ArrayList<Coords> neighbors = new ArrayList<>();
        neighbors.add(new Coords(coords.getX() + 1, coords.getY()));
        neighbors.add(new Coords(coords.getX() - 1, coords.getY()));
        neighbors.add(new Coords(coords.getX(), coords.getY() + 1));
        neighbors.add(new Coords(coords.getX(), coords.getY() - 1));

        return neighbors;
    }
}

