package model.chat;

import model.player.Player;

import java.util.HashMap;
import java.util.Map;

public class Message {
    private final String text;
    private final Player sender;
    private final boolean isGlobal;

    public Message(String text, Player sender,boolean global){
        this.sender=sender;
        this.text=text;
        this.isGlobal=global;
    }

    public String getText() {
        return text;
    }

    public Player getSender() {
        return sender;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void sendMessage(Player player){
        if(player==null){
            for( HashMap.Entry<Integer, Player> entry: player.getGame().getPlayers().entrySet()){
                player.getPlayerChat().addMessage(this.sender,this,true);
            }
        } else{
            player.getPlayerChat().addMessage(this.sender,this,false);
        }
    }
}
