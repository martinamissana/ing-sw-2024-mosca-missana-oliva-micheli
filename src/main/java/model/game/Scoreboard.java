package model.game;

import model.player.Player;

import java.util.HashMap;

public class Scoreboard {
    private HashMap <Player, Integer> playerScore;
    private Game Game;
    public int getScore(Player player){
       return playerScore.get(player);
    }

    public Player getGameWinner(){

        return null;

    }

    public void setScore(Player player,int points){

        playerScore.put(player,points);

    }
}
