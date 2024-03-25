package model.game;

import model.player.Player;

import java.util.HashMap;
import java.util.Map;

public class Game {
   private int numOfPlayer;
   private HashMap<Integer, Player> players;

    public int getNumOfPlayer() {
        return numOfPlayer;
    }

    public HashMap<Integer, Player> getPlayers() {
        return players;
    }
}
