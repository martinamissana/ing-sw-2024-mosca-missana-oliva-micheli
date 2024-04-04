package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.player.Player;

import java.util.HashMap;

public class Game {

    private final int gameID;
   private final int numOfPlayer;
   private final HashMap<Player, Integer> players;
   private int whosTurn;
   private HashMap<Player,Integer> scoreboard;

    public Game(int gameID,int numOfPlayer, HashMap<Player, Integer> players,HashMap<Player,Integer> scoreboard) {
        this.numOfPlayer = numOfPlayer;
        this.players = players;
        this.gameID=gameID;
        this.whosTurn=0;
        this.scoreboard=scoreboard;
    }

    public void setWhosTurn(int whosTurn) {
        this.whosTurn = whosTurn;
    }

    public int getWhosTurn() {
        return whosTurn;
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

    public void setPlayePoints(Player player,int points) {
        this.scoreboard.put(player,points);
    }

    public int getScoreboard(Player player) {
        return scoreboard.get(player);
    }
}
