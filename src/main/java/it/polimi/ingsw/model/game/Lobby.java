package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.player.PawnBuffer;
import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Lobby class, it represents a waiting room that players can join. When the number of players required is reached, the game is created
 */
public class Lobby implements Serializable {

    private final int ID;
    private final ArrayList<Player> players;
    private final int numOfPlayers;
    private final PawnBuffer pawnBuffer;


    /**
     * Constructor
     *
     * @param id the id that identifies the lobby
     * @param numOfPlayers number of players required
     */
    public Lobby(int id, int numOfPlayers) {
        ID = id;
        this.numOfPlayers = numOfPlayers;
        this.players = new ArrayList<>();
        this.pawnBuffer = new PawnBuffer();
    }

    /**
     * getter
     *
     * @return int numOfPlayers
     */
    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    /**
     * getter
     *
     * @return ArrayList<Player> players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * getter
     *
     * @return PawnBuffer, the class where there are stored the available pawns
     */
    public PawnBuffer getPawnBuffer() {return pawnBuffer;}

    /**
     * method to add players to the lobby
     *
     * @param player player to add to the lobby
     * @throws FullLobbyException the lobby is full
     */
    public void addPlayer(Player player) throws FullLobbyException {
        if (lobbyFull()) {
            throw new FullLobbyException();
        }
        players.add(player);
    }

    /**
     * method to remove players from the lobby
     *
     * @param player the player you wish to remove
     */
    public void removePlayer(Player player) {
        players.remove(player);
    }

    /**
     * method that returns true if the lobby is full
     *
     * @return boolean
     */
    private boolean lobbyFull() {
        return players.size() >= numOfPlayers;
    }

    /**
     * returns the lobby ID
     *
     * @return int
     */
    public int getID() {
        return ID;
    }
}
