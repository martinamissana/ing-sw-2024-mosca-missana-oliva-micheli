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

    public void setView(View view) {
        this.view = view;
        view.addObserver(this);
        setDecks();
    }

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

    private void refresh(DeckTypeBox deck) {        // TODO: Clean ???
        Platform.runLater(()-> {
            switch (deck){
                case DeckType.RESOURCE -> {
                    Card topResDeck = view.getTopResourceCard();
                    if (topResDeck != null) {
                        if (topResDeck.getSide().equals(CardSide.FRONT)) topResDeck.flip();
                        resDeck.getChildren().add(new CardBuilder(topResDeck).getCardImage());
                    }
                }
                case DeckType.GOLDEN -> {
                    Card topGoldDeck = view.getTopGoldenCard();
                    if (topGoldDeck != null) {
                        if (topGoldDeck.getSide().equals(CardSide.FRONT))
                            topGoldDeck.flip();
                        goldDeck.getChildren().add(new CardBuilder(topGoldDeck).getCardImage());
                    }
                }
                case DeckBufferType.RES1 -> {
                    Card deckBuffRes1 = view.getDeckBuffers().get(DeckBufferType.RES1).getCard();
                    if (deckBuffRes1 != null) {
                        if (deckBuffRes1.getSide().equals(CardSide.BACK))
                            deckBuffRes1.flip();
                        res1.getChildren().add(new CardBuilder(deckBuffRes1).getCardImage());
                        if (view.getTopResourceCard().getSide().equals(CardSide.FRONT)) view.getTopResourceCard().flip();
                        resDeck.getChildren().add(new CardBuilder(view.getTopResourceCard()).getCardImage());
                    }
                }
                case DeckBufferType.RES2 -> {
                    Card deckBuffRes2 = view.getDeckBuffers().get(DeckBufferType.RES2).getCard();
                    if (deckBuffRes2 != null){
                        if (deckBuffRes2.getSide().equals(CardSide.BACK))
                            deckBuffRes2.flip();
                        res2.getChildren().add(new CardBuilder(deckBuffRes2).getCardImage());
                        if (view.getTopResourceCard().getSide().equals(CardSide.FRONT)) view.getTopResourceCard().flip();
                        resDeck.getChildren().add(new CardBuilder(view.getTopResourceCard()).getCardImage());
                    }
                }
                case DeckBufferType.GOLD1 -> {
                    Card deckBuffGold1 = view.getDeckBuffers().get(DeckBufferType.GOLD1).getCard();
                    if (deckBuffGold1 != null){
                        if (deckBuffGold1.getSide().equals(CardSide.BACK))
                            deckBuffGold1.flip();
                        gold1.getChildren().add(new CardBuilder(deckBuffGold1).getCardImage());
                        if (view.getTopGoldenCard().getSide().equals(CardSide.FRONT)) view.getTopGoldenCard().flip();
                        goldDeck.getChildren().add(new CardBuilder(view.getTopGoldenCard()).getCardImage());
                    }
                }
                case DeckBufferType.GOLD2 -> {
                    Card deckBuffGold2 = view.getDeckBuffers().get(DeckBufferType.GOLD2).getCard();
                    if (deckBuffGold2 != null){
                        if (deckBuffGold2.getSide().equals(CardSide.BACK))
                            deckBuffGold2.flip();
                        gold2.getChildren().add(new CardBuilder(deckBuffGold2).getCardImage());
                        if (view.getTopGoldenCard().getSide().equals(CardSide.FRONT)) view.getTopGoldenCard().flip();
                        goldDeck.getChildren().add(new CardBuilder(view.getTopGoldenCard()).getCardImage());
                    }
                }
                default -> {}
            }
        });
    }


}
