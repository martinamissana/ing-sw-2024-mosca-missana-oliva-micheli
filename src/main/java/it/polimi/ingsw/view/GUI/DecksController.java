package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.controller.exceptions.IllegalActionException;
import it.polimi.ingsw.controller.exceptions.NotYourTurnException;
import it.polimi.ingsw.controller.exceptions.UnexistentUserException;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.deck.DeckType;
import it.polimi.ingsw.model.deck.DeckTypeBox;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.game.Action;
import it.polimi.ingsw.model.game.GamePhase;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.s2c.CardDrawnFromSourceMessage;
import it.polimi.ingsw.network.netMessage.s2c.GameWinnersAnnouncedMessage;
import it.polimi.ingsw.network.netMessage.s2c.LobbyLeftMessage;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Class DecksController
 * sets the decks/deckBuffers images and allows the player to draw from them
 */
public class DecksController implements ViewObserver {
    private View view;
    @FXML
    private Pane resDeck;
    @FXML
    private Pane goldDeck;
    @FXML
    private Pane res1;
    @FXML
    private Pane res2;
    @FXML
    private Pane gold1;
    @FXML
    private Pane gold2;

    /**
     * draws the clicked card from the deck/deckBuffer
     * @param mouseEvent identifies the deck/deckBuffer clicked
     */
    @FXML
    public void drawCard(MouseEvent mouseEvent) {
        if (!view.isYourTurn() || !view.getAction().equals(Action.DRAW) || view.getGamePhase()!= GamePhase.PLAYING_GAME)
            return;
        if (mouseEvent.getSource() == resDeck) {
            try {
                view.drawCard(DeckType.RESOURCE);
            } catch (IllegalActionException | NotYourTurnException | EmptyBufferException | GameDoesNotExistException |
                     HandIsFullException | LobbyDoesNotExistException | EmptyDeckException ignored) {
            } catch (IOException | UnexistentUserException e) {
                throw new RuntimeException(e);
            }
            resDeck.getChildren().clear();
            return;
        }
        if (mouseEvent.getSource() == goldDeck) {
            try {
                view.drawCard(DeckType.GOLDEN);
            } catch (IllegalActionException | NotYourTurnException | EmptyBufferException | GameDoesNotExistException |
                     HandIsFullException | LobbyDoesNotExistException | EmptyDeckException ignored) {
            } catch (IOException | UnexistentUserException e) {
                throw new RuntimeException(e);
            }
            goldDeck.getChildren().clear();
            return;
        }
        if (mouseEvent.getSource() == res1) {
            try {
                view.drawCard(DeckBufferType.RES1);
            } catch (IllegalActionException | NotYourTurnException | EmptyBufferException | GameDoesNotExistException |
                     HandIsFullException | LobbyDoesNotExistException | EmptyDeckException ignored) {
            } catch (IOException | UnexistentUserException e) {
                throw new RuntimeException(e);
            }
            res1.getChildren().clear();
            return;
        }
        if (mouseEvent.getSource() == res2) {
            try {
                view.drawCard(DeckBufferType.RES2);
            } catch (IllegalActionException | NotYourTurnException | EmptyBufferException | GameDoesNotExistException |
                     HandIsFullException | LobbyDoesNotExistException | EmptyDeckException ignored) {
            } catch (IOException | UnexistentUserException e) {
                throw new RuntimeException(e);
            }
            res2.getChildren().clear();
            return;
        }
        if (mouseEvent.getSource() == gold1) {
            try {
                view.drawCard(DeckBufferType.GOLD1);
            } catch (IllegalActionException | NotYourTurnException | EmptyBufferException | GameDoesNotExistException |
                     HandIsFullException | LobbyDoesNotExistException | EmptyDeckException ignored) {
            } catch (IOException | UnexistentUserException e) {
                throw new RuntimeException(e);
            }
            gold1.getChildren().clear();
            return;
        }
        if (mouseEvent.getSource() == gold2) {
            try {
                view.drawCard(DeckBufferType.GOLD2);
            } catch (IllegalActionException | NotYourTurnException | EmptyBufferException | GameDoesNotExistException |
                     HandIsFullException | LobbyDoesNotExistException | EmptyDeckException ignored) {
            } catch (IOException | UnexistentUserException e) {
                throw new RuntimeException(e);
            }
            gold2.getChildren().clear();
        }
    }

    /**
     * @param view the observable view
     */
    public void setView(View view) {
        this.view = view;
        view.addObserver(this);
        setDecks();
    }

    /**
     * initialize decks and deckBuffers
     */
    public void setDecks(){
        if(view.getTopResourceCard().getSide() == CardSide.FRONT) view.getTopResourceCard().flip();
        resDeck.getChildren().add(new CardBuilder(view.getTopResourceCard()).getCardImage());
        if(view.getTopGoldenCard().getSide() == CardSide.FRONT) view.getTopGoldenCard().flip();
        goldDeck.getChildren().add(new CardBuilder(view.getTopGoldenCard()).getCardImage());
        for (DeckBufferType d : view.getDeckBuffers().keySet())
        {
            if(view.getDeckBuffers().get(d).getCard().getSide() == CardSide.BACK) view.getDeckBuffers().get(d).getCard().flip();
        }
        res1.getChildren().add(new CardBuilder(view.getDeckBuffers().get(DeckBufferType.RES1).getCard()).getCardImage());
        res2.getChildren().add(new CardBuilder(view.getDeckBuffers().get(DeckBufferType.RES2).getCard()).getCardImage());
        gold1.getChildren().add(new CardBuilder(view.getDeckBuffers().get(DeckBufferType.GOLD1).getCard()).getCardImage());
        gold2.getChildren().add(new CardBuilder(view.getDeckBuffers().get(DeckBufferType.GOLD2).getCard()).getCardImage());
    }


    @Override
    public void update(NetMessage message) throws IOException {
        switch (message) {
            case CardDrawnFromSourceMessage m -> refresh(m.getType());
            case LobbyLeftMessage ignored -> view.removeObserver(this);
            case GameWinnersAnnouncedMessage ignored -> view.removeObserver(this);
            default -> {}
        }
    }

    /**
     * changes the decks images when a card is drawn
     * @param deck the deck/deckBuffer changed
     */
    private void refresh(DeckTypeBox deck) {
        Platform.runLater(()-> {
            switch (deck) {
                case DeckType.RESOURCE -> refreshDeck(DeckType.RESOURCE);
                case DeckType.GOLDEN -> refreshDeck(DeckType.GOLDEN);
                case DeckBufferType.RES1 -> {
                    refreshBuffer(res1, DeckBufferType.RES1);
                    refreshDeck(DeckType.RESOURCE);
                }
                case DeckBufferType.RES2 -> {
                    refreshBuffer(res2, DeckBufferType.RES2);
                    refreshDeck(DeckType.RESOURCE);
                }
                case DeckBufferType.GOLD1 -> {
                    refreshBuffer(gold1, DeckBufferType.GOLD1);
                    refreshDeck(DeckType.GOLDEN);
                }
                case DeckBufferType.GOLD2 -> {
                    refreshBuffer(gold2, DeckBufferType.GOLD2);
                    refreshDeck(DeckType.GOLDEN);
                }
                default -> {}
            }
        });
    }

    /**
     * Update images when card is drawn from deck
     * @param type type of the deck buffer
     */
    public void refreshDeck(DeckType type) {
        if (type.equals(DeckType.RESOURCE)) {
            resDeck.getChildren().clear();
            Card card = view.getTopResourceCard();
            if (card != null) {
                if (card.getSide().equals(CardSide.FRONT)) card.flip();
                resDeck.getChildren().add(new CardBuilder(card).getCardImage());
            }
        } else {
            goldDeck.getChildren().clear();
            Card card = view.getTopGoldenCard();
            if (card != null) {
                if (card.getSide().equals(CardSide.FRONT)) card.flip();
                goldDeck.getChildren().add(new CardBuilder(card).getCardImage());
            }
        }
    }

    /**
     * Update images when card is drawn from deck buffer
     * @param deckBuffer Pane of the deck buffer
     * @param type type of the deck buffer
     */
    public void refreshBuffer(Pane deckBuffer, DeckBufferType type) {
        switch (type) {
            case RES1 -> res1.getChildren().clear();
            case RES2 -> res2.getChildren().clear();
            case GOLD1 -> gold1.getChildren().clear();
            case GOLD2 -> gold2.getChildren().clear();
        }

        Card card = view.getDeckBuffers().get(type).getCard();
        if (card != null) {
            if (card.getSide().equals(CardSide.BACK)) card.flip();
            deckBuffer.getChildren().add(new CardBuilder(card).getCardImage());
        }
    }
}
