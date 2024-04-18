package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.deck.DeckType;
import it.polimi.ingsw.model.exceptions.HandIsFullException;
import it.polimi.ingsw.controller.exceptions.IllegalActionException;
import it.polimi.ingsw.model.exceptions.IllegalMoveException;
import it.polimi.ingsw.model.game.Action;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.deck.DeckBuffer;
import it.polimi.ingsw.model.player.Coords;

public class GameController {

    /**
     * plays a card from the hand of a game's current player to the field, at the specified position
     * @param game
     * @param handPos
     * @param coords
     * @throws IllegalMoveException
     */
    public void playCard(Game game, int handPos, Coords coords) throws IllegalActionException, IllegalMoveException {

        // if the game's state isn't PLAY, leave method
        if (game.getAction() != Action.PLAY)
            throw new IllegalActionException();

        // get the game's current player
        Player currPlayer = game.getCurrPlayer();

        // get card in player's specified hand position
        ResourceCard card = (ResourceCard) currPlayer.getHand().getCard(handPos);

        // place card in the field and calculate points
        int points = currPlayer.getField().addCard((ResourceCard) card, coords);

        // remove card from player's hand
        currPlayer.getHand().removeCard(card);

        // add points to player's score
        game.addToScore(currPlayer, points);
    }

    /**
     * draws a card from deck and adds it to the game's current player's hand
     * @param game
     * @param deckType
     * @throws HandIsFullException
     */
    public void drawCard(Game game, DeckType deckType) throws IllegalActionException, HandIsFullException {

        // if the game's state isn't DRAW, leave method
        if (game.getAction() != Action.DRAW)
            throw new IllegalActionException();

        ResourceCard newCard = game.getDeck(deckType).draw();
        game.getCurrPlayer().getHand().addCard(newCard);
    }

    /**
     * draws a card from deckBuffer and adds it to the game's current player's hand
     * @param game
     * @param deckBuffer
     * @throws HandIsFullException
     */
    public void drawCard(Game game, String deckBuffer) throws IllegalActionException, HandIsFullException {

        // if the game's state isn't DRAW, leave method
        if (game.getAction() != Action.DRAW)
            throw new IllegalActionException();

        ResourceCard newCard = game.getDeckBuffer(deckBuffer).draw();
        game.getCurrPlayer().getHand().addCard(newCard);
    }

    /**
     * updates the game's "whoseTurn" attribute and sets it to the next player's playerID
     * @param game
     */
    public void nextTurn(Game game) { game.setWhoseTurn((game.getWhoseTurn()+1)%game.getNumOfPlayer()); }
}