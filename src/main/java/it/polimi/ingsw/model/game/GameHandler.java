package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exceptions.GameDoesNotExistException;
import it.polimi.ingsw.model.exceptions.LobbyDoesNotExistException;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.observer.Observable;
import it.polimi.ingsw.model.observer.events.LoginEvent;
import it.polimi.ingsw.model.player.Player;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Class GameHandler<br>
 * Stores all the information on lobbies and games hosted in a server
 */
public class GameHandler extends Observable implements Serializable {
    private int numOfLobbies = 0;
    private final HashMap<Integer, Lobby> lobbies;
    private final HashMap<Integer, Game> activeGames;
    private final ArrayList<Player> users;

    /**
     * Class constructor
     */
    public GameHandler() {
        this.lobbies = new HashMap<>();
        this.activeGames = new HashMap<>();
        this.users = new ArrayList<>();
    }

    /**
     * @return the number of existing lobbies
     */
    public synchronized int getNumOfLobbies() { return numOfLobbies; }

    /**
     * @param numOfLobbies the current number of lobbies
     */
    public synchronized void setNumOfLobbies(int numOfLobbies) { this.numOfLobbies = numOfLobbies; }

    /**
     * @return the map containing all existing lobbies
     */
    public synchronized HashMap<Integer, Lobby> getLobbies() { return lobbies; }

    /**
     * @param ID of the desired lobby
     * @return the lobby corresponding to the specified ID
     * @throws LobbyDoesNotExistException thrown if no existing lobby has the specified ID
     */
    public synchronized Lobby getLobby(Integer ID) throws LobbyDoesNotExistException {
        if (lobbies.containsKey(ID))
            return lobbies.get(ID);
        throw new LobbyDoesNotExistException();
    }

    /**
     * @return the map containing all active games
     */
    public synchronized HashMap<Integer, Game> getActiveGames() { return activeGames; }

    /**
     * @param ID ID of the desired game
     * @return the game corresponding to the specified ID
     * @throws GameDoesNotExistException thrown if no active game has the specified ID
     */
    public synchronized Game getGame(Integer ID) throws GameDoesNotExistException {
        if (activeGames.containsKey(ID))
            return activeGames.get(ID);
        throw new GameDoesNotExistException();
    }

    /**
     * @return the list of all connected users
     */
    public synchronized ArrayList<Player> getUsers() { return users; }

    /**
     * @param user the user to add to the user list
     * @throws NicknameAlreadyTakenException thrown when the user's nickname is equal to another user's
     */
    public synchronized void addUser(Player user) throws NicknameAlreadyTakenException, IOException {
        for (Player u : this.users)
            if (u.getNickname().equals(user.getNickname()))
                throw new NicknameAlreadyTakenException();
        getUsers().add(user);
        notify(new LoginEvent(user.getNickname()));
    }

    /**
     * @param nickname nickname of the user to remove
     */
    public synchronized void removeUser(String nickname) {
        List<Player> copiedUsers = new ArrayList<>(users);
        for (Player p : copiedUsers)
            if (p.getNickname().equals(nickname))
                users.remove(p);
    }
}