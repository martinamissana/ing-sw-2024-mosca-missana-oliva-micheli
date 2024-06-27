package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.player.PawnBuffer;
import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class Lobby<br>
 * It represents a waiting room that players can join. When the required number of players
 * is reached, and each one has chosen a pawn, a game with the corresponding lobby's ID is automatically created.
 */
public class Lobby implements Serializable {

    private final int ID;
    private final int numOfPlayers;
    private final ArrayList<Player> players;
    private final PawnBuffer pawnBuffer;


    /**
     * Class constructor
     * @param id the lobby's ID
     * @param numOfPlayers number of players required to start the match
     */
    public Lobby(int id, int numOfPlayers) {
        ID = id;
        this.numOfPlayers = numOfPlayers;
        this.players = new ArrayList<>();
        this.pawnBuffer = new PawnBuffer();
    }

    /**
     * @return the lobby's {@code ID}
     */
    public int getID() { return ID; }

    /**
     * @return the number of players required to start the match
     */
    public int getNumOfPlayers() { return numOfPlayers; }

    /**
     * @return the list of players who are currently in the lobby
     */
    public ArrayList<Player> getPlayers() { return players; }

    /**
     * @return the lobby's {@code PawnBuffer}
     */
    public PawnBuffer getPawnBuffer() { return pawnBuffer; }

    /**
     * @param player player to add to the lobby
     * @throws FullLobbyException thrown if the lobby is already full
     */
    public void addPlayer(Player player) throws FullLobbyException {
        if (lobbyFull())
            throw new FullLobbyException();
        players.add(player);
    }

    /**
     * @param player the player to remove from the lobby
     */
    public void removePlayer(Player player) { players.remove(player); }

    /**
     * @return {@code true} if the lobby is full, {@code false} otherwise.
     */
    private boolean lobbyFull() { return players.size() >= numOfPlayers; }
}
