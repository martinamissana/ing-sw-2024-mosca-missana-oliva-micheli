package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exceptions.GameDoesNotExistException;
import it.polimi.ingsw.model.exceptions.LobbyDoesNotExistsException;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.observer.Observable;
import it.polimi.ingsw.model.player.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Class GameHandler
 * stores all the data about multiple games and lobbies hosted in a server
 */
public class GameHandler extends Observable implements Serializable  {
    private HashMap<Integer,Game> activeGames;
    private HashMap<Integer,Lobby> lobbies;
    private int numOfGames=0;
    private int numOfLobbies=0;
    private final ArrayList<Player> users;

    /**
     * Class constructor
     */
    public GameHandler() {
        this.activeGames = new HashMap<>();
        this.lobbies = new HashMap<>();
        this.users=new ArrayList<>();
    }

    /**
     * getter
     * @return activeGames - the HashMap with the active games
     */
    public HashMap<Integer, Game> getActiveGames() {
        return activeGames;
    }

    /**
     * getter
     * @return lobbies - the HashMap with the  current lobbies
     */
    public HashMap<Integer, Lobby> getLobbies() { return lobbies; }

    /**
     * getter
     * @return users - the list of users connected
     */
    public ArrayList<Player> getUsers() {
        return users;
    }

    /**
     * removes a user from the user list
     * @param user - is the user you want to remove
     */
    public void removeUser(Player user){
        this.users.remove(user);
    }

    /**
     * getter
     * @return numOfGames - the number of games that have been created
     */
    public int getNumOfGames() {
        return numOfGames;
    }

    /**
     * getter
     * @return numOfLobbies - the number of lobbies that have been created
     */
    public int getNumOfLobbies() { return numOfLobbies; }

    /**
     * used to get a specific game
     * @param ID of the game you want to get from the list of active games
     * @return the specified game
     * @throws GameDoesNotExistException if the game doesn't exist
     */
    public Game getGame(Integer ID) throws GameDoesNotExistException {
        if (activeGames.containsKey(ID)) {
            return activeGames.get(ID);
        } else {
            throw new GameDoesNotExistException("Game with ID " + ID + " does not exist");
        }
    }

    /**
     * used to get a specific lobby
     * @param ID - of the lobby you want to get from the list of lobbies
     * @return the specified lobby
     * @throws LobbyDoesNotExistsException if the lobby doesn't exist
     */
    public Lobby getLobby(Integer ID) throws LobbyDoesNotExistsException {
        if (lobbies.containsKey(ID)) {
            return lobbies.get(ID);
        } else {
            throw new LobbyDoesNotExistsException("Lobby with ID " + ID + " does not exist");
        }
    }

    /**
     * setter
     * @param numOfGames- the value you want to set for the number of active games
     */
    public void setNumOfGames(int numOfGames) { this.numOfGames = numOfGames; }

    /**
     * setter
     * @param numOfLobbies - the value you want to set for the number of lobbies
     */
    public void setNumOfLobbies(int numOfLobbies) { this.numOfLobbies = numOfLobbies; }
    public void addUser(Player user) throws NicknameAlreadyTakenException {
        for (Player u: this.users){
            if(u.getNickname().equals(user.getNickname())) throw new NicknameAlreadyTakenException();
        }
        getUsers().add(user);
    }

    /**
     * used to save the GameHandler status with all the data about all active games and lobbies
     * @throws IOException - produced by failed or interrupted I/O operations
     */
    public void save() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("./data.ser");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(this);
        fileOutputStream.close();
        objectOutputStream.close();
    }

    /**
     * used to load all the data about active games and lobbies in case the server crashes
     * @throws IOException - produced by failed or interrupted I/O operations
     * @throws ClassNotFoundException  -  if no definition for the class with the specified name could be found
     */
    public void load() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("./data.ser");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        GameHandler deserialized = (GameHandler) objectInputStream.readObject();
        this.activeGames=deserialized.activeGames;
        this.lobbies=deserialized.lobbies;
        fileInputStream.close();
        objectInputStream.close();
    }
}
