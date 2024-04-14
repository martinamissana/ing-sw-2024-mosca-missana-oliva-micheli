package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Lobby class, it represents a waiting room that players can join. When the number of players required is reached, the game is created
 */
public class Lobby implements Serializable {

    private ArrayList<Player> players;
    private int numOfPlayers;

    /**
     * Constructor
     * @param numOfPlayers - number of players required
     */
    public Lobby(int numOfPlayers){
        this.numOfPlayers=numOfPlayers;
        this.players=new ArrayList<>();
    }

    /**
     * getter
     * @return int - numOfPlayers
     */
    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    /**
     * getter
     * @return ArrayList<Player> - players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * method to add players to the lobby
     * @param  player - player to add to the lobby
     * @throws FullLobbyException - the lobby is full
     * @throws NicknameAlreadyTakenException - someone in the lobby has the same nickname
     */
    public void addPlayer(Player player) throws FullLobbyException, NicknameAlreadyTakenException {
        if (lobbyFull()) {
            throw new FullLobbyException();
        }
        for (Player p: players){
            if(p.getNickname().equals(player.getNickname()))
               throw new NicknameAlreadyTakenException();
        }
        players.add(player);
    }

    /**
     * method to remove players from the lobby
     * @param player
     */
    public void removePlayer(Player player){
        players.remove(player);

    }

    /**
     * method that returns true if the lobby is full
     * @return boolean
     */
    private boolean lobbyFull(){
        return players.size() >= numOfPlayers;
    }
}
