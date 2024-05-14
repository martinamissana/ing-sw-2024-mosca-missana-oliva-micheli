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

public class GameCreatedMessage extends NetMessage {
    private final Integer ID;
    private final Player firstPlayer;
    private final HashMap<Player, Integer> scoreboard = new HashMap<>();
    private final HashMap<DeckBufferType, DeckBuffer> deckBuffers = new HashMap<>();
    private final Card topResourceCard;
    private final Card topGoldenCard;
    private final Goal commonGoal1;
    private final Goal commonGoal2;
    private final GamePhase gamePhase;
    private final Action action;


    public GameCreatedMessage(Integer ID, Player firstPlayer, HashMap<Player, Integer> scoreboard, Card topResourceCard, Card topGoldenCard, Goal commonGoal1, Goal commonGoal2, GamePhase gamePhase,DeckBuffer d1,DeckBuffer d2,DeckBuffer d3,DeckBuffer d4) {
        this.ID=ID;
        this.firstPlayer = firstPlayer;
        this.scoreboard.putAll(scoreboard);
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

    public Integer getID() {return ID;}

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public HashMap<Player, Integer> getScoreboard() {
        return scoreboard;
    }

    public HashMap<DeckBufferType, DeckBuffer> getDeckBuffers() {
        return deckBuffers;
    }

    public Card getTopResourceCard() {
        return topResourceCard;
    }

    public Card getTopGoldenCard() {
        return topGoldenCard;
    }

    public Goal getCommonGoal1() {
        return commonGoal1;
    }

    public Goal getCommonGoal2() {
        return commonGoal2;
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public Action getAction() {
        return action;
    }
}
