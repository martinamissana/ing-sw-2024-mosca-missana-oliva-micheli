package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.model.player.Player;

import java.util.HashMap;

public class Game {

    private final int gameID;
    private final int numOfPlayer;
    private final HashMap<Player, Integer> players;
    private int whoseTurn;
    private final HashMap<Player,Integer> scoreboard;
    private Goal commonGoal1;
    private Goal commonGoal2;

    public Game(int gameID,int numOfPlayer, HashMap<Player, Integer> players,HashMap<Player,Integer> scoreboard) {
        this.numOfPlayer = numOfPlayer;
        this.players = players;
        this.gameID=gameID;
        this.commonGoal1 = null;
        this.commonGoal2 = null;
        this.whoseTurn=0;
        this.scoreboard=scoreboard;
    }

    public void setWhoseTurn(int whoseTurn) {
        this.whoseTurn = whoseTurn;
    }

    public int getWhoseTurn() {
        return whoseTurn;
    }

    public int getGameID() {
        return gameID;
    }

    public int getNumOfPlayer() {
        return numOfPlayer;
    }

    public HashMap<Player, Integer> getPlayers() {
        return players;
    }

    public void save(){

    }

    public Goal getCommonGoal1() {
        return commonGoal1;
    }

    public Goal getCommonGoal2() {
        return commonGoal2;
    }

    public void setCommonGoal1(Goal commonGoal1) {
        this.commonGoal1 = commonGoal1;
    }

    public void setCommonGoal2(Goal commonGoal2) {
        this.commonGoal2 = commonGoal2;
    }

    public void setPlayerPoints(Player player,int points) {
        this.scoreboard.put(player,points);
    }

    public int getScoreboard(Player player) {
        return scoreboard.get(player);
    }
}
