package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exceptions.GameDoesNotExistException;
import it.polimi.ingsw.model.exceptions.LobbyDoesNotExistsException;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.player.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Class GameHandler
 * stores all the data about multiple games and lobbies hosted in a server
 */
public class GameHandler implements Serializable {
    private HashMap<Integer,Game> activeGames;
    private HashMap<Integer,Lobby> lobbies;
    private int numOfGames=0;
    private int numOfLobbies=0;
    private ArrayList<Player> users;

    /**
     * Class constructor
     */
    public GameHandler() {
        this.activeGames = new HashMap<>();
        this.lobbies = new HashMap<>();
        this.users=new ArrayList<>();
    }

    /**
     * gets the HashMap with the active games
     * @return activeGames
     */
    public HashMap<Integer, Game> getActiveGames() {
        return activeGames;
    }

    /**
     * gets the HashMap with the  current lobbies
     * @return lobbies
     */
    public HashMap<Integer, Lobby> getLobbies() { return lobbies; }

    public ArrayList<Player> getUsers() {
        return users;
    }

    /**
     * gets the number of games that have been created
     * @return numOfGames
     */
    public int getNumOfGames() {
        return numOfGames;
    }

    /**
     * gets the number of lobbies that have been created
     * @return numOfLobbies
     */
    public int getNumOfLobbies() { return numOfLobbies; }

    /**
     * used to get a specific game from the list of active games, returns an exception if it doesn't exist
     * @param ID
     * @return the specified game
     * @throws GameDoesNotExistException
     */
    public Game getGame(int ID) throws GameDoesNotExistException {
        if (activeGames.containsKey(ID)) {
            return activeGames.get(ID);
        } else {
            throw new GameDoesNotExistException("Game with ID " + ID + " does not exist");
        }
    }

    /**
     * used to get a specific lobby from the list of lobbies, returns an exception if it doesn't exist
     * @param ID
     * @return the specified lobby
     * @throws LobbyDoesNotExistsException
     */
    public Lobby getLobby(Integer ID) throws LobbyDoesNotExistsException {
        if (lobbies.containsKey(ID)) {
            return lobbies.get(ID);
        } else {
            throw new LobbyDoesNotExistsException("Lobby with ID " + ID + " does not exist");
        }
    }

    /**
     * sets numOfGames to the value in input
     * @param numOfGames
     */
    public void setNumOfGames(int numOfGames) { this.numOfGames = numOfGames; }

    /**
     * sets numOfLobbies to the value in input
     * @param numOfLobbies
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
     * @throws IOException
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
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void load() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("./data.ser");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        GameHandler deserialized = (GameHandler) objectInputStream.readObject();
        this.activeGames=deserialized.activeGames;
        this.lobbies=deserialized.lobbies;
        this.numOfLobbies=deserialized.numOfLobbies;
        this.numOfGames=deserialized.numOfGames;
        this.activeGames=deserialized.activeGames;


    }
}
