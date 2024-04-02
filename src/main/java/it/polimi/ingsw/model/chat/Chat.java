package it.polimi.ingsw.model.chat;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class Chat
 * each Player has an associated chat with a list of sent messages and a list of received messages
 */
public class Chat {
    private final ArrayList<Message> sentMessages;
    private final ArrayList<Message> receivedMessages;

    /**
     * Class constructor
     */
    public Chat(){
        this.sentMessages = new ArrayList<Message>();
        this.receivedMessages = new ArrayList<Message>();
    }

    /**
     * gets the received messages
     * @return receivedMessages
     */
    public ArrayList<Message> getReceivedMessages() {
        return receivedMessages;
    }
    /**
     * gets the sent messages
     * @return sentMessages
     */
    public ArrayList<Message> getSentMessages() {
        return sentMessages;
    }

    /**
     * gets the messages received from a specified player
     * @param sender
     * @return list of messages from sender
     */
    public ArrayList<Message> getReceivedMessagesFromPlayer(Player sender) {
        int i;
        ArrayList<Message> list= new ArrayList<Message>();
        for(i=0;i<=receivedMessages.size();i++){
            if(receivedMessages.get(i).getSender()==sender){
                list.add(receivedMessages.get(i));
            }
        }
        return list;
    }

    /**
     * gets the messages sent to a specified player
     * @param receiver
     * @return list of messages to receiver
     */
    public ArrayList<Message> getSentMessagesToPlayer(Player receiver) {
        int i;
        ArrayList<Message> list= new ArrayList<Message>();
        for(i=0;i<=sentMessages.size();i++){
            if(sentMessages.get(i).getSender()==receiver){
                list.add(sentMessages.get(i));
            }
        }
        return list;
    }

    /**
     * gets the messages received in the global chat
     * @return list of global received message
     */
    public ArrayList<Message> getGlobalReceivedMessages() {
        int i;
        ArrayList<Message> list= new ArrayList<Message>();
        for(i=0;i<=receivedMessages.size();i++){
            if(receivedMessages.get(i).isGlobal()){
                list.add(receivedMessages.get(i));
            }
        }
        return list;
    }

    /**
     * gets the messages sent in the global chat
     * @return list of global sent messages
     */
    public ArrayList<Message> getGlobalSentMessages() {
        int i;
        ArrayList<Message> list= new ArrayList<Message>();
        for(i=0;i<=sentMessages.size();i++){
            if(sentMessages.get(i).isGlobal()){
                list.add(sentMessages.get(i));
            }
        }
        return list;
    }
}
