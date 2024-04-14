package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exceptions.GameDoesNotExistException;
import it.polimi.ingsw.model.exceptions.LobbyDoesNotExistsException;
import it.polimi.ingsw.model.player.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class GameHandler implements Serializable {

    private HashMap<Integer,Game> activeGames;
    private HashMap<Integer,Lobby> lobbies;
    private int numOfGames=0;
    private int numOfLobbies=0;

    public GameHandler() {
        this.activeGames = new HashMap<>();
        this.lobbies = new HashMap<>();
    }

    public HashMap<Integer, Game> getActiveGames() {
        return activeGames;
    }

    public HashMap<Integer, Lobby> getLobbies() { return lobbies; }

    public Game getGame(int ID) throws GameDoesNotExistException {
        if (activeGames.containsKey(ID)) {
            return activeGames.get(ID);
        } else {
            throw new GameDoesNotExistException("Game with ID " + ID + " does not exist");
        }
    }
    public Lobby getLobby(Integer ID) throws LobbyDoesNotExistsException {
        if (lobbies.containsKey(ID)) {
            return lobbies.get(ID);
        } else {
            throw new LobbyDoesNotExistsException("Lobby with ID " + ID + " does not exist");
        }
    }
    public int getNumOfGames() {
        return numOfGames;
    }
    public int getNumOfLobbies() { return numOfLobbies; }

    public void setNumOfGames(int numOfGames) {
       this.numOfGames = numOfGames;
    }

    public void setNumOfLobbies(int numOfLobbies) {
        this.numOfLobbies = numOfLobbies;
    }

    public void save() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("./data.ser");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(this);
        fileOutputStream.close();
        objectOutputStream.close();
    }
    public void load() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("./data.ser");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        GameHandler deserialized = (GameHandler) objectInputStream.readObject();
        this.activeGames=deserialized.activeGames;
        this.lobbies=deserialized.lobbies;

    }
}
