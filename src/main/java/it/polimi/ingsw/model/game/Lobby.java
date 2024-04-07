package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;

import java.io.Serializable;
import java.util.HashSet;

public class Lobby implements Serializable {

    private HashSet<String> players;
    private int numOfPlayers;

    public Lobby(int numOfPlayers){
        this.numOfPlayers=numOfPlayers;
        this.players=new HashSet<String>();
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public HashSet<String> getPlayers() {
        return players;
    }

    public void addPlayer(String player) throws FullLobbyException, NicknameAlreadyTakenException {
            if (players.size() == numOfPlayers) {
                throw new FullLobbyException();
            }
            if (players.contains(player)) {
                throw new NicknameAlreadyTakenException();
            }
        players.add(player);
    }

    public void removePlayer(String player){
        players.remove(player);

    }

    public boolean lobbyFull(){
        return players.size() >= numOfPlayers;
    }
}
