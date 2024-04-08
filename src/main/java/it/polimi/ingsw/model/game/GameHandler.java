package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exceptions.GameDoesNotExistException;
import it.polimi.ingsw.model.player.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class GameHandler implements Serializable {

    private HashMap<Integer,Game> activeGames;
    private ArrayList<Lobby> lobbies;
    private static int numOfGames=0;

    public GameHandler() {
        this.activeGames = new HashMap<>();
        this.lobbies = new ArrayList<Lobby>();
    }

    public HashMap<Integer, Game> getActiveGames() {
        return activeGames;
    }

    public ArrayList<Lobby> getLobbies() {
        return lobbies;
    }

    public Game getGame(int ID) throws GameDoesNotExistException {
        if (activeGames.containsKey(ID)) {
            return activeGames.get(ID);
        } else {
            throw new GameDoesNotExistException("Game with ID " + ID + " does not exist");
        }
    }


    public static int getNumOfGames() {
        return numOfGames;
    }


    public void createLobby(int numOfPlayers){
        lobbies.add(new Lobby(numOfPlayers));
    }


    public void createGame(Lobby lobby){
        HashMap<Integer,Player> playerIntegerHashMap=new HashMap<>();
        HashSet<String> players=lobby.getPlayers();
        int i=0;
        for(String p:players) {
            playerIntegerHashMap.put(i,new Player(p, false,null));
            i++;
        }
        //scoreboard initialization->all values set at 0
        HashMap<Player,Integer> scoreboard= new HashMap<>();
        for(Integer key : playerIntegerHashMap.keySet()){
            scoreboard.put(playerIntegerHashMap.get(key),0);
        }
        activeGames.put(numOfGames,new Game(numOfGames,lobby.getNumOfPlayers(),playerIntegerHashMap,scoreboard));
        numOfGames++;
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
        //this.sentMessages = deserializedChat.sentMessages
        //return deserializedChat;

    }
}
