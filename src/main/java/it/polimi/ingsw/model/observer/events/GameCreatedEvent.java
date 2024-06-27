package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.deck.DeckBuffer;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.game.Action;
import it.polimi.ingsw.model.game.GamePhase;
import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.model.player.Player;

import java.util.HashMap;

/**
 * GameCreatedEvent class<br>
 * Extends abstract class Event<br>
 * Used to notify the virtual view of the clients that the game has been created
 */
public class GameCreatedEvent extends Event {
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
     * @param ID ID of the game
     * @param firstPlayer player who starts first
     * @param scoreboard scoreboard with the player points
     * @param topResourceCard card on top of ResDeck
     * @param topGoldenCard card on top of GoldDeck
     * @param commonGoal1 first common goal
     * @param commonGoal2 second common goal
     * @param gamePhase phase of the game
     * @param d1 deckbuffer Res1
     * @param d2 deckbuffer Res2
     * @param d3 deckbuffer Gold1
     * @param d4 deckbuffer Gold2
     */
    public GameCreatedEvent(Integer ID, Player firstPlayer, HashMap<Player, Integer> scoreboard, Card topResourceCard, Card topGoldenCard, Goal commonGoal1, Goal commonGoal2, GamePhase gamePhase, DeckBuffer d1, DeckBuffer d2, DeckBuffer d3, DeckBuffer d4) {
        this.ID = ID;
        this.firstPlayer = firstPlayer;
        this.scoreboard = scoreboard;
        this.topResourceCard = topResourceCard;
        this.topGoldenCard = topGoldenCard;
        this.commonGoal1 = commonGoal1;
        this.commonGoal2 = commonGoal2;
        this.gamePhase = gamePhase;
        this.deckBuffers.put(DeckBufferType.RES1, d1);
        this.deckBuffers.put(DeckBufferType.RES2, d2);
        this.deckBuffers.put(DeckBufferType.GOLD1, d3);
        this.deckBuffers.put(DeckBufferType.GOLD2, d4);
        this.action=Action.PLAY;
    }

    /**
     * @return Integer gameID
     */
    public Integer getID() { return ID; }

    /**
     * @return player who's the first player
     */
    public Player getFirstPlayer() { return firstPlayer; }

    /**
     * @return scoreboard with the players' scores
     */
    public HashMap<Player, Integer> getScoreboard() { return scoreboard; }

    /**
     * @return deckbuffers
     */
    public HashMap<DeckBufferType, DeckBuffer> getDeckBuffers() { return deckBuffers; }

    /**
     * @return the top card of the resource card
     */
    public Card getTopResourceCard() { return topResourceCard; }

    /**
     * @return the top card of the golden card
     */
    public Card getTopGoldenCard() { return topGoldenCard; }

    /**
     * @return the fist common goal
     */
    public Goal getCommonGoal1() { return commonGoal1; }

    /**
     * @return the second common goal
     */
    public Goal getCommonGoal2() { return commonGoal2; }

    /**
     * @return the game phase
     */
    public GamePhase getGamePhase() { return gamePhase; }

    /**
     * @return the action [draw-play]
     */
    public Action getAction() { return action; }
}
