package model.game;

import model.player.Player;

import java.util.Map;

public class Game {
   private int numOfPlayer;
   private Map<Integer, Player> players;

    public int getNumOfPlayer() {
        return numOfPlayer;
    }

    public Map<Integer, Player> getPlayers() {
        return players;
    }
}
