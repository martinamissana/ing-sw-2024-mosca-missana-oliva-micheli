package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.deck.*;
import it.polimi.ingsw.model.exceptions.EmptyBufferException;
import it.polimi.ingsw.model.exceptions.EmptyDeckException;
import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.model.goal.GoalContainer;
import it.polimi.ingsw.model.player.Player;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class Game<br>
 * It represents a single instance of a Codex Naturalis match.
 */
public class Game implements Serializable {

    private final int gameID;
    private final int numOfPlayers;
    private final ArrayList<Player> players;
    private int whoseTurn;
    private Action action = Action.PLAY;
    private boolean lastRound; // set to true when, at the end of a round, a player has reached 20 points that round
    private final HashMap<Player, Integer> scoreboard;
    private Goal commonGoal1;
    private Goal commonGoal2;
    private final Deck ResourceDeck = new Deck(DeckType.RESOURCE);
    private final Deck GoldenDeck = new Deck(DeckType.GOLDEN);
    private final HashMap<DeckBufferType, DeckBuffer> deckBuffers = new HashMap<>();
    private final GoalContainer goals;
    private GamePhase gamePhase;
    private final ArrayList<Player> winners = new ArrayList<>();


    /**
     * Class constructor
     * @param gameID       the game's ID, which univocally identifies it
     * @param numOfPlayers number of players of the game
     * @param players      HashMap that maps playerID to Player
     * @param scoreboard   HashMap that maps each player to their score
     * @throws IOException thrown if I/O operations are interrupted or failed
     */
    public Game(int gameID, int numOfPlayers, ArrayList<Player> players, HashMap<Player, Integer> scoreboard) throws IOException {
        this.gameID = gameID;
        this.numOfPlayers = numOfPlayers;
        this.players = players;
        this.whoseTurn = 0;
        this.goals = new GoalContainer();
        this.commonGoal1 = null;
        this.commonGoal2 = null;
        this.scoreboard = scoreboard;
        this.deckBuffers.put(DeckBufferType.GOLD1, new DeckBuffer(this.GoldenDeck));
        this.deckBuffers.put(DeckBufferType.GOLD2, new DeckBuffer(this.GoldenDeck));
        this.deckBuffers.put(DeckBufferType.RES1, new DeckBuffer(this.ResourceDeck));
        this.deckBuffers.put(DeckBufferType.RES2, new DeckBuffer(this.ResourceDeck));
        this.gamePhase = GamePhase.PLACING_STARTER_CARD;
    }

    /**
     * @return the game's ID
     */
    public int getGameID() { return gameID; }

    /**
     * @return the number of players in the match
     */
    public int getNumOfPlayers() { return numOfPlayers; }

    /**
     * @return the list of players ordered by turn in any round
     */
    public ArrayList<Player> getPlayers() { return players; }

    /**
     * @return the player who is currently playing
     */
    public Player getCurrPlayer() { return players.get(whoseTurn); }

    /**
     * @return the index of the current player in the players list
     */
    public int getWhoseTurn() { return whoseTurn; }

    /**
     * @param whoseTurn int, indicates the index in the ArrayList players of who is playing when the method is called
     */
    public void setWhoseTurn(int whoseTurn) { this.whoseTurn = whoseTurn; }

    /**
     * @return the action the player is allowed to perform at the moment
     */
    public Action getAction() { return action; }

    /**
     * @param action the action the player is allowed to perform at the moment
     */
    public void setAction(Action action) { this.action = action; }

    /**
     * @return {@code true} if the current round is the last in the match; meaning that,
     * in the previous round, either at least one player has reached 20 points or
     * both decks were emptied of their cards. {@code false} otherwise.
     */
    public boolean isLastRound() { return lastRound; }

    /**
     * @param lastRound desired value
     */
    public void setLastRound(boolean lastRound) { this.lastRound = lastRound; }

    /**
     * @return list of goals
     */
    public GoalContainer getGoals() { return this.goals; }

    /**
     * @return one of the two goals common to all players in the match
     */
    public Goal getCommonGoal1() { return commonGoal1; }

    /**
     * @return one of the two goals common to all players in the match
     */
    public Goal getCommonGoal2() { return commonGoal2; }

    /**
     * @param commonGoal1 one of the two common goals
     */
    public void setCommonGoal1(Goal commonGoal1) { this.commonGoal1 = commonGoal1; }

    /**
     * @param commonGoal2 one of the two common goals
     */
    public void setCommonGoal2(Goal commonGoal2) { this.commonGoal2 = commonGoal2; }

    /**
     * @return the entire scoreboard
     */
    public HashMap<Player, Integer> getScoreboard() { return scoreboard; }

    /**
     * @param player desired player to get the score of
     * @return the score of the specified player
     */
    public int getPlayerScore(Player player) { return scoreboard.get(player); }

    /**
     * Increments a player's score by a set amount of points
     * @param player the player that will have their score incremented
     * @param points the amount of points to add to the player's score
     */
    public void addToScore(Player player, int points) { scoreboard.put(player, scoreboard.get(player) + points); }

    /**
     * @param type the type of deck to return
     * @return the game's deck of the specified type
     */
    public Deck getDeck(DeckType type) {
        if (type == DeckType.RESOURCE) return ResourceDeck;
        else return GoldenDeck;
    }

    /**
     * @return the game's resource deck
     */
    public Deck getResourceDeck() { return ResourceDeck; }

    /**
     * @return the game's golden deck
     */
    public Deck getGoldenDeck() { return GoldenDeck; }

    /**
     * @param type specifies the type of the deck buffer needed
     * @return a reference to the corresponding {@code DeckBuffer}
     */
    public DeckBuffer getDeckBuffer(DeckBufferType type) { return deckBuffers.get(type); }

    /**
     * Draws a card from the specified source ({@code Deck} or {@code DeckBuffer})
     * @param src source to draw the card from
     * @return a card drawn from the selected source
     * @throws EmptyDeckException thrown if the selected source has no cards
     */
    public ResourceCard drawFromSource(DeckTypeBox src) throws EmptyDeckException, EmptyBufferException {
        switch (src) {
            case DeckType.RESOURCE -> { return getResourceDeck().draw(); }
            case DeckType.GOLDEN -> { return getGoldenDeck().draw(); }
            case DeckBufferType.RES1 -> { return getDeckBuffer(DeckBufferType.RES1).draw(); }
            case DeckBufferType.RES2 -> { return getDeckBuffer(DeckBufferType.RES2).draw(); }
            case DeckBufferType.GOLD1 -> { return getDeckBuffer(DeckBufferType.GOLD1).draw(); }
            case DeckBufferType.GOLD2 -> { return getDeckBuffer(DeckBufferType.GOLD2).draw(); }
            default -> throw new IllegalStateException("Unexpected value: " + src);
        }
    }

    /**
     * @return the phase the game is currently in
     */
    public GamePhase getGamePhase() { return gamePhase; }

    /**
     * @param gamePhase the phase the game will be in
     */
    public void setGamePhase(GamePhase gamePhase) { this.gamePhase = gamePhase; }

    /**
     * @return the list of winners of the game
     */
    public ArrayList<Player> getWinners() { return winners; }

    /**
     * @param winners list of the winners of the game
     */
    public void setWinners(ArrayList<Player> winners) { this.winners.addAll(winners); }
}