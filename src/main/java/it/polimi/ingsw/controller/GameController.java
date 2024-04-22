package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.NotYourTurnException;
import it.polimi.ingsw.model.deck.DeckTypeBox;
import it.polimi.ingsw.controller.exceptions.IllegalActionException;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.game.Action;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.GameHandler;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.player.Coords;

public class GameController extends Controller {

    /**
     * lass constructor
     *
     * @param gh
     */
    public GameController(GameHandler gh) {
        super(gh);
    }

    /**
     * plays a card from the hand of a player to their field, at the specified position
     * @param gameID ID of the calling player's game
     * @param player player who's playing the card
     * @param handPos card's position index in the player's hand
     * @param coords position in the field to play the selected card to
     * @throws GameDoesNotExistException thrown if the given ID does not correspond to any Game
     * @throws NotYourTurnException thrown when a player tries to perform an action when it's not their turn
     * @throws IllegalActionException thrown if the selected game's current expected action isn't PLAY
     * @throws IllegalMoveException thrown if the selected card cannot be played to the field as requested
     */
    public void playCard(Integer gameID, Player player, int handPos, Coords coords) throws GameDoesNotExistException, NotYourTurnException, IllegalActionException, IllegalMoveException {

        // get game from ID
        Game game = gh.getGame(gameID);

        // if it's not this player's turn
        if (player.getNickname().equals(game.getCurrPlayer().getNickname()))
            throw new NotYourTurnException();

        // if the game's state isn't PLAY
        if (game.getAction() != Action.PLAY)
            throw new IllegalActionException();

        // get card in player's specified hand position
        ResourceCard card = (ResourceCard) player.getHand().getCard(handPos);

        // place card in the field and calculate points
        int points = player.getField().addCard(card, coords);

        // remove card from player's hand
        player.getHand().removeCard(card);

        // add points to player's score
        game.addToScore(player, points);

        // set the game's current action to DRAW after playing a card
        // only if it's not the last round (because if it is, players cannot draw cards)
        if (!game.isLastRound())
            game.setAction(Action.DRAW);
    }

    /**
     * draws a card from a player's game's deck or deckBuffer and adds it to their hand
     * @param gameID ID of the game of the drawing player
     * @param player player who's drawing the card
     * @param deckTypeBox type of card source to draw from
     * @throws GameDoesNotExistException thrown if the given ID does not correspond to any Game
     * @throws NotYourTurnException thrown when a player tries to perform an action when it's not their turn
     * @throws IllegalActionException thrown if the selected game's current expected action isn't DRAW
     * @throws HandIsFullException thrown if the selected game's current player has their hand full
     * @throws EmptyDeckException thrown if the selected deck is out of cards
     * @throws EmptyBufferException thrown if the selected deck buffer is out of cards
     */
    public void drawCard(Integer gameID, Player player, DeckTypeBox deckTypeBox) throws GameDoesNotExistException, NotYourTurnException, IllegalActionException, HandIsFullException, EmptyDeckException, EmptyBufferException {

        // get game from ID
        Game game = gh.getGame(gameID);

        // if it's not this player's turn
        if (player.getNickname().equals(game.getCurrPlayer().getNickname()))
            throw new NotYourTurnException();

        // if the game's state isn't DRAW
        if (game.getAction() != Action.DRAW)
            throw new IllegalActionException();

        // draw a card and add it to the current player's hand
        ResourceCard newCard = game.drawFromSource(deckTypeBox);
        player.getHand().addCard(newCard);

        // set the game's current action to PLAY after drawing a card
        game.setAction(Action.PLAY);
    }

    /**
     * updates the game's "whoseTurn" attribute and sets it to the next player's playerID
     * @param gameID ID of the game to advance the turn of
     * @throws GameDoesNotExistException thrown if the given ID does not correspond to any Game
     */
    public void nextTurn(Integer gameID) throws GameDoesNotExistException {

        // get game from ID
        Game game = gh.getGame(gameID);

        // update turn counter
        game.setWhoseTurn((game.getWhoseTurn()+1)%game.getNumOfPlayers());
    }
}