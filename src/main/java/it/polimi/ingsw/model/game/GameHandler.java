package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.card.GoldenCard;
import it.polimi.ingsw.model.chat.Chat;
import it.polimi.ingsw.model.commonItem.Kingdom;
import it.polimi.ingsw.model.player.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class GameHandler implements Serializable {

    private ArrayList<Game> activeGames;
    private ArrayList<Lobby> lobbies;

    public GameHandler() {
        this.activeGames = new ArrayList<Game>();
        this.lobbies = new ArrayList<Lobby>();
    }

    public ArrayList<Game> getActiveGames() {
        return activeGames;
    }

    public ArrayList<Lobby> getLobbies() {
        return lobbies;
    }

    public void createLobby(int numOfPlayers){
        lobbies.add(new Lobby(numOfPlayers));
    }
    public Game loadGame() {
        //need to deserialize the file
        return null;
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
        activeGames.add(new Game(activeGames.size(),lobby.getNumOfPlayers(),playerIntegerHashMap,scoreboard));
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
