package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class GameHandler implements Serializable {

    private ArrayList<Game> activeGames;
    private ArrayList<Lobby> lobbies;

    public void createLobby(int numOfPlayers){
        lobbies.add(new Lobby(numOfPlayers));
    }
    public Game loadGame() {
        //need to deserialize the file
        return null;
    }

    public GameHandler() {
        lobbies=new ArrayList<>();
        activeGames=new ArrayList<>();
    }

    public void createGame(Lobby lobby){
        HashMap<Player,Integer> playerIntegerHashMap=new HashMap<>();
        HashSet<String> players=lobby.getPlayers();
        int i=0;
        for(String p:players)
        {
                playerIntegerHashMap.put(new Player(p, false,null), i);
                i++;
        }
        HashMap<Player,Integer> scoreboard= playerIntegerHashMap;
        for (Player p: scoreboard.keySet()){
            scoreboard.replace(p,scoreboard.get(p),0);
        }
        activeGames.add(new Game(activeGames.size(),lobby.getNumOfPlayers(),playerIntegerHashMap,scoreboard));
    }


}
