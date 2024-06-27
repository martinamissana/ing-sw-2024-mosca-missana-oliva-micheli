package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.deck.DeckBuffer;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.game.Action;
import it.polimi.ingsw.model.game.GamePhase;
import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

import java.util.HashMap;

/**
 * Class GameCreatedMessage
 * used to inform the views that the game has been created
 */
public class GameCreatedMessage extends NetMessage {
    private final Integer ID;
    private final Player firstPlayer;
    private final HashMap<Player, Integer> scoreboard;
    private final HashMap<DeckBufferType, DeckBuffer> deckBuffers = new HashMap<>();
    private final Card topResourceCard;
    private final Card topGoldenCard;
    private final Goal commonGoal1;
    private final Goal commonGoal2;
    private final GamePhase gamePhase;
    private final Action action;


    /**
     * Class constructor
     * @param ID the game ID
     * @param firstPlayer the player who plays first
     * @param scoreboard the points of each player
     * @param topResourceCard the top card of the resource deck
     * @param topGoldenCard the top card of the golden deck
     * @param commonGoal1 the first common goal
     * @param commonGoal2 the second common goal
     * @param gamePhase the starting phase of the game
     * @param d1 the first resource deckBuffer
     * @param d2 the second resource deckBuffer
     * @param d3 the first golden deckBuffer
     * @param d4 the second golden deckBuffer
     */
    public GameCreatedMessage(Integer ID, Player firstPlayer, HashMap<Player, Integer> scoreboard, Card topResourceCard, Card topGoldenCard, Goal commonGoal1, Goal commonGoal2, GamePhase gamePhase,DeckBuffer d1,DeckBuffer d2,DeckBuffer d3,DeckBuffer d4) {
        this.ID=ID;
        this.firstPlayer = firstPlayer;
        this.scoreboard = new HashMap<>(scoreboard);
        this.topResourceCard = topResourceCard;
        this.topGoldenCard = topGoldenCard;
        this.commonGoal1 = commonGoal1;
        this.commonGoal2 = commonGoal2;
        this.gamePhase = gamePhase;
        this.deckBuffers.put(DeckBufferType.RES1,d1);
        this.deckBuffers.put(DeckBufferType.RES2,d2);
        this.deckBuffers.put(DeckBufferType.GOLD1,d3);
        this.deckBuffers.put(DeckBufferType.GOLD2,d4);
        this.action=Action.PLAY;
    }

    /**
     * @return Integer
     */
    public Integer getID() {return ID;}

    /**
     * @return Player
     */
    public Player getFirstPlayer() {
        return firstPlayer;
    }

    /**
     * @return HashMap with score of each player
     */
    public HashMap<Player, Integer> getScoreboard() {
        return scoreboard;
    }

    /**
     * @return HashMap of deckBuffers
     */
    public HashMap<DeckBufferType, DeckBuffer> getDeckBuffers() {
        return deckBuffers;
    }

    /**
     * @return Card
     */
    public Card getTopResourceCard() {
        return topResourceCard;
    }

    /**
     * @return Card
     */
    public Card getTopGoldenCard() {
        return topGoldenCard;
    }

    /**
     * @return Goal
     */
    public Goal getCommonGoal1() {
        return commonGoal1;
    }

    /**
     * @return Goal
     */
    public Goal getCommonGoal2() {
        return commonGoal2;
    }

    /**
     * @return GamePhase
     */
    public GamePhase getGamePhase() {
        return gamePhase;
    }

    /**
     * @return Action
     */
    public Action getAction() {
        return action;
    }
}
