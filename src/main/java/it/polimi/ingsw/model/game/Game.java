package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.player.Player;

import java.util.HashMap;

public class Game {
   private int numOfPlayer;
   private HashMap<Integer, Player> players;

    public Game(int numOfPlayer, HashMap<Integer, Player> players) {
        this.numOfPlayer = numOfPlayer;
        this.players = players;
    }

    public int getNumOfPlayer() {
        return numOfPlayer;
    }

    public HashMap<Integer, Player> getPlayers() {
        return players;
    }
}
