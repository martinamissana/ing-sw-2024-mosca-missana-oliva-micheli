package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.deck.*;
import it.polimi.ingsw.model.exceptions.EmptyDeckException;
import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.model.player.PawnBuffer;
import it.polimi.ingsw.model.player.Player;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Game class, it represents a single instance of a match
 */
public class Game implements Serializable {

    private final int gameID;
    private final int numOfPlayers;
    private final ArrayList<Player> players;
    private int whoseTurn;
    private Action action= Action.PLAY;
    private boolean lastRound;//set to true afterward someone reached 20 points in a round and that same round has finished
    private final HashMap<Player,Integer> scoreboard;
    private Goal commonGoal1;
    private Goal commonGoal2;
    private final Deck ResourceDeck=new Deck(DeckType.RESOURCE);
    private final Deck GoldenDeck=new Deck(DeckType.GOLDEN);
    private final HashMap<DeckBufferType,DeckBuffer> deckBuffers = new HashMap<>();
    private final PawnBuffer pawnBuffer;


    /**
     * Class Constructor
     * @param gameID - identifies univocally the game
     * @param numOfPlayers - number of players of the game
     * @param players - HashMap<Integer, Player> maps PlayerID to Player
     * @param scoreboard - HashMap<Player,Integer> maps the player to his points
     * @throws IOException - produced by failed or interrupted I/O operations
     */
    public Game(int gameID, int numOfPlayers, ArrayList<Player> players, HashMap<Player,Integer> scoreboard) throws IOException {
        this.numOfPlayers = numOfPlayers;
        this.players = players;
        this.gameID=gameID;
        this.commonGoal1 = null;
        this.commonGoal2 = null;
        this.whoseTurn=0;
        this.scoreboard=scoreboard;
        this.deckBuffers.put(DeckBufferType.GOLD1,new DeckBuffer(this.GoldenDeck));
        this.deckBuffers.put(DeckBufferType.GOLD2,new DeckBuffer(this.GoldenDeck));
        this.deckBuffers.put(DeckBufferType.RES1,new DeckBuffer(this.ResourceDeck));
        this.deckBuffers.put(DeckBufferType.RES2,new DeckBuffer(this.ResourceDeck));
        this.pawnBuffer=new PawnBuffer();
    }

    /**
     * getter
     * @param type - specifies the type of the deckbuffer needed (gold1,gold2,res1,res2)
     * @return Deckbuffer
     */
    public DeckBuffer getDeckBuffer(DeckBufferType type){
        return deckBuffers.get(type);
    }

    /**
     * Setter
     * @param whoseTurn - int, indicates the PlayerID of who is playing at the moment
     */
    public void setWhoseTurn(int whoseTurn) {
        this.whoseTurn = whoseTurn;
    }

    /**
     * getter
     * @return int - PlayerID of who is playing at the moment
     */
    public int getWhoseTurn() {
        return whoseTurn;
    }

    /**
     * getter
     * @return int - GameID
     */
    public int getGameID() {
        return gameID;
    }

    /**
     * getter
     * @return int- NumOfPlayer
     */
    public int getNumOfPlayer() {
        return numOfPlayers;
    }

    /**
     * getter
     * @return HashMap<Integer,Player> - players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * getter
     * @return the action the player is allowed to do at that moment
     */
    public Action getAction() {
        return action;
    }

    /**
     * getter
     * @return PawnBuffer - the class where there are stored the available pawns
     */
    public PawnBuffer getPawnBuffer() {return pawnBuffer;}

    /**
     * setter
     * @param action the action the player is allowed to do at that moment
     */
    public void setAction(Action action) {
        this.action = action;
    }

    /**
     * getter
     * @return boolean isLastRound - the boolean that specify if it is the last round of the game
     */
    public boolean isLastRound() {
        return lastRound;
    }

    /**
     * setter
     * @param lastRound - the boolean that specify if it is the last round of the game
     */
    public void setLastRound(boolean lastRound) {
        this.lastRound = lastRound;
    }

    /**
     * getter
     * @return Goal - one of the two common goals
     */
    public Goal getCommonGoal1() {
        return commonGoal1;
    }

    /**
     * getter
     * @return Goal - one of the two common goals
     */
    public Goal getCommonGoal2() {
        return commonGoal2;
    }

    /**
     * setter
     * @param commonGoal1 - Goal
     */
    public void setCommonGoal1(Goal commonGoal1) {
        this.commonGoal1 = commonGoal1;
    }

    /**
     * setter
     * @param commonGoal2 - Goal
     */
    public void setCommonGoal2(Goal commonGoal2) {
        this.commonGoal2 = commonGoal2;
    }

    /**
     * increments the points of a player
     * @param player - the player that will have their points incremented
     * @param points - the amount of points added to the player score
     */
    public void addToScore(Player player,int points){
        scoreboard.put(player,scoreboard.get(player)+points);
    }

    /**
     * setter
     * @param player - the player that will have their points set
     * @param points - the amount of points set to the player score
     */
    public void setPlayerScore(Player player,int points) {
        this.scoreboard.put(player,points);
    }

    /**
     * getter
     * @param player - the player that you want to get the points from
     * @return int - the points of the specified player
     */
    public int getPlayerScore(Player player) {
        return scoreboard.get(player);
    }

    /**
     * getter
     * @return Player - the player who is playing at the moment
     */
    public Player getCurrPlayer(){
        return players.get(whoseTurn);
    }

    /**
     * getter
     * @return HashMap<Player,Integer> - the entire scoreboard
     */
    public HashMap<Player, Integer> getScoreboard() {
        return scoreboard;
    }

    /**
     * getter
     * @return Deck - ResourceDeck of the game
     */
    public Deck getResourceDeck() {
        return ResourceDeck;
    }

    /**
     * getter
     * @return Deck - GoldenDeck of the game
     */
    public Deck getGoldenDeck() {
        return GoldenDeck;
    }

    /**
     * getter of a specific deck
     * @param type - defines the type of deck wanted
     * @return Deck
     */
    public Deck getDeck(DeckType type){
        if(type==DeckType.RESOURCE) return ResourceDeck;
        else return GoldenDeck;
    }

    /**
     * draws a card from the specified source (Deck or DeckBuffer)
     * @param src source to draw the card from
     * @return ResourceCard
     * @throws EmptyDeckException thrown if the source has no cards
     */
    public ResourceCard drawFromSource(DeckTypeBox src) throws EmptyDeckException {
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
}