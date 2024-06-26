package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exceptions.GameDoesNotExistException;
import it.polimi.ingsw.model.exceptions.LobbyDoesNotExistException;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.observer.Observable;
import it.polimi.ingsw.model.observer.events.LoginEvent;
import it.polimi.ingsw.model.player.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Class GameHandler
 * stores all the data about multiple games and lobbies hosted in a server
 */
public class GameHandler extends Observable implements Serializable {
    private HashMap<Integer, Game> activeGames;
    private HashMap<Integer, Lobby> lobbies;
    private int numOfLobbies = 0;
    private final ArrayList<Player> users;

    /**
     * Class constructor
     */
    public GameHandler() {
        this.activeGames = new HashMap<>();
        this.lobbies = new HashMap<>();
        this.users = new ArrayList<>();
    }

    /**
     * getter
     *
     * @return activeGames the HashMap with the active games
     */
    public synchronized HashMap<Integer, Game> getActiveGames() {
        return activeGames;
    }

    /**
     * getter
     *
     * @return lobbies the HashMap with the  current lobbies
     */
    public synchronized HashMap<Integer, Lobby> getLobbies() {
        return lobbies;
    }

    /**
     * getter
     *
     * @return users the list of users connected
     */
    public synchronized ArrayList<Player> getUsers() {
        return users;
    }

    /**
     * getter
     *
     * @return numOfLobbies the number of lobbies that have been created
     */
    public synchronized int getNumOfLobbies() { return numOfLobbies; }

    /**
     * used to get a specific game
     *
     * @param ID of the game you want to get from the list of active games
     * @return the specified game
     * @throws GameDoesNotExistException if the game doesn't exist
     */
    public synchronized Game getGame(Integer ID) throws GameDoesNotExistException {
        if (activeGames.containsKey(ID)) {
            return activeGames.get(ID);
        } else {
            throw new GameDoesNotExistException();
        }
    }

    /**
     * used to get a specific lobby
     *
     * @param ID of the lobby you want to get from the list of lobbies
     * @return the specified lobby
     * @throws LobbyDoesNotExistException if the lobby doesn't exist
     */
    public synchronized Lobby getLobby(Integer ID) throws LobbyDoesNotExistException {
        if (lobbies.containsKey(ID)) {
            return lobbies.get(ID);
        } else {
            throw new LobbyDoesNotExistException();
        }
    }

    /**
     * setter
     *
     * @param numOfLobbies the value you want to set for the number of lobbies
     */
    public synchronized void setNumOfLobbies(int numOfLobbies) { this.numOfLobbies = numOfLobbies; }

    /**
     * used to add users to user list
     *
     * @param user the user that will be added to the user list
     * @throws NicknameAlreadyTakenException when in user list there is already a user with the same nickname
     */
    public synchronized void addUser(Player user) throws NicknameAlreadyTakenException, IOException {
        for (Player u : this.users) {
            if (u.getNickname().equals(user.getNickname())) throw new NicknameAlreadyTakenException();
        }
        getUsers().add(user);
        notify(new LoginEvent(user.getNickname()));
    }

    public synchronized void removeUser(String nickname) {
        List<Player> copiedUsers = new ArrayList<>(users);

        for (Player p : copiedUsers) {
            if (p.getNickname().equals(nickname)) users.remove(p);
        }
    }

}
