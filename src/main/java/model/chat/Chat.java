package model.chat;
import model.game.Game;
import model.player.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chat {
    private Player nickname;
    private HashMap<Player, List<Message>> directMessages;
    private ArrayList<Message> globalMessages;
    private Game game;


    public Chat(){
        for (Map.Entry<Integer, Player> entry : game.getPlayers().entrySet()) {
            Player K = entry.getValue();
            directMessages.put(K, null);
        }
    }

    public HashMap<Player, List<Message>> getDirectMessages() {
        return directMessages;
    }

    public ArrayList<Message> getGlobalMessages() {
        return globalMessages;
    }

    public void addMessage(Player sender, Message message, boolean global){
        if(global==false){
            directMessages.get(sender).add(message);
        } else {
            globalMessages.add(message);
        }
    }
}
