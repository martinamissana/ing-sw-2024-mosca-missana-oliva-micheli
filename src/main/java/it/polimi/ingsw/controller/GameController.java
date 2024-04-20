package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.deck.DeckTypeBox;
import it.polimi.ingsw.controller.exceptions.IllegalActionException;
import it.polimi.ingsw.model.exceptions.IllegalMoveException;
import it.polimi.ingsw.model.exceptions.HandIsFullException;
import it.polimi.ingsw.model.exceptions.EmptyDeckException;
import it.polimi.ingsw.model.game.Action;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.player.Coords;

public class GameController {

    /**
     * plays a card from the hand of a game's current player to the field, at the specified position
     * @param game game of the calling player
     * @param handPos card's position index in the player's hand
     * @param coords position in the field to play the selected card to
     * @throws IllegalActionException thrown if the selected game's current expected action isn't PLAY
     * @throws IllegalMoveException thrown if the selected card cannot be played to the field as requested
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
        int points = currPlayer.getField().addCard(card, coords);

        // remove card from player's hand
        currPlayer.getHand().removeCard(card);

        // add points to player's score
        game.addToScore(currPlayer, points);

        // set the game's current action to DRAW after playing a card
        game.setAction(Action.DRAW);
    }

    /**
     * draws a card from a deck or deckBuffer and adds it to the game's current player's hand
     * @param game game of the drawing player
     * @param deckTypeBox type of card source to draw from
     * @throws IllegalActionException thrown if the selected game's current expected action isn't DRAW
     * @throws HandIsFullException thrown if the selected game's current player has their hand full
     * @throws EmptyDeckException thrown if the selected source is out of cards
     */
    public void drawCard(Game game, DeckTypeBox deckTypeBox) throws IllegalActionException, HandIsFullException, EmptyDeckException {

        // if the game's state isn't DRAW, leave method
        if (game.getAction() != Action.DRAW)
            throw new IllegalActionException();

        // draw a card and add it to the current player's hand
        ResourceCard newCard = game.drawFromSource(deckTypeBox);
        game.getCurrPlayer().getHand().addCard(newCard);

        // set the game's current action to PLAY after drawing a card
        game.setAction(Action.PLAY);
    }

    /**
     * updates the game's "whoseTurn" attribute and sets it to the next player's playerID
     * @param game game to advance the turn of
     */
    public void nextTurn(Game game) { game.setWhoseTurn((game.getWhoseTurn()+1)%game.getNumOfPlayer()); }
}