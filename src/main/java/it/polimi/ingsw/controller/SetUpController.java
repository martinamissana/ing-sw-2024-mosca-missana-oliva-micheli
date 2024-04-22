package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.game.CardsPreset;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.GameHandler;
import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.model.goal.GoalBuffer;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.PawnBuffer;
import it.polimi.ingsw.model.player.Player;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class SetUpController extends Controller implements Serializable {
    public SetUpController(GameHandler gh) {
        super(gh);
    }

    /**
     * Set decks and deck buffers
     * @param gameID - ID of the game played
     * @throws GameDoesNotExistException - if gameID does not correspond to a game in game handler
     */
    public void setGameArea(Integer gameID) throws GameDoesNotExistException {
        Game game = gh.getGame(gameID);

        // setting decks and deck buffers:
        game.getResourceDeck().shuffle();
        game.getGoldenDeck().shuffle();
        for(DeckBufferType type : DeckBufferType.values()) game.getDeckBuffer(type).refill();
    }

    /**
     * @param gameID - ID of the game played
     * @throws GameDoesNotExistException - if gameID does not correspond to a game in game handler
     * @throws IOException - for building the starter deck
     */
    public void giveStarterCards(Integer gameID) throws GameDoesNotExistException, IOException {
        Game game = gh.getGame(gameID);
        ArrayList<Player> players = game.getPlayers();

        // Creating starter deck + drawing starter cards for all players:
        ArrayList<StarterCard> starter = CardsPreset.getStarterCards();
        Collections.shuffle(starter);

        for (Player p : players) {
            try {
                p.getHand().addCard(starter.removeLast());
            }
            catch(HandIsFullException ignored) {}   // doesn't suppose to happen
        }
    }

    /**
     * Allows the player to choose a pawn
     * @param gameID - ID of the game played
     * @param player - who is choosing the pawn
     * @param color - color of the desired pawn
     * @throws PawnAlreadyTakenException - if the chosen pawn has already been taken
     * @throws GameDoesNotExistException - if gameID does not correspond to a game in game handler
     */
    public void choosePawn(Integer gameID, Player player, Pawn color) throws PawnAlreadyTakenException, GameDoesNotExistException {
        Game game = gh.getGame(gameID);
        PawnBuffer pawnList = game.getPawnBuffer();

        if(pawnList.getPawnList().contains(color)) pawnList.getPawnList().remove(color);
        else throw new PawnAlreadyTakenException();

        player.setPawn(color);
    }

    /**
     * Allows the player to choose the side of the starter card before placing it down
     * @param player - who is playing the card
     * @param side - side chosen by the player
     */
    public void chooseCard(Player player, CardSide side) {
        StarterCard card = (StarterCard) player.getHand().getCard(0);
        if(!card.getSide().equals(side)) card.flip();

        player.getField().addCard(card);
        player.getHand().removeCard(card);
    }

    /**
     * Fill all players' hands with 2 resource cards and 1 golden card
     * @param gameID - ID of the game played
     * @throws GameDoesNotExistException - if gameID does not correspond to a game in game handler
     * @throws EmptyDeckException - (doesn't suppose to happen)
     * @throws HandIsFullException - if in hands are already present other cards
     */
    public void fillHands(Integer gameID) throws GameDoesNotExistException, EmptyDeckException, HandIsFullException {
        Game game = gh.getGame(gameID);

        for (Player p : game.getPlayers()) {
            p.getHand().addCard(game.getResourceDeck().draw());
            p.getHand().addCard(game.getResourceDeck().draw());
            p.getHand().addCard(game.getGoldenDeck().draw());
        }
    }

    public void chooseGoals(Integer gameID, GoalBuffer goals) throws GameDoesNotExistException {
        Game game = gh.getGame(gameID);
        game.setCommonGoal1(goals.getgoal());
        game.setCommonGoal2(goals.getgoal());
    }

    public void choosePersonalGoal(Player player, Goal goal1, Goal goal2, Goal choice) {
        if (choice.equals(goal1) || choice.equals(goal2)) player.setPrivateGoal(choice);
    }
}
