package it.polimi.ingsw.model.chat;
import it.polimi.ingsw.model.player.Player;

import java.io.*;
import java.util.ArrayList;

/**
 * Class Chat
 * each Player has an associated chat with a list of sent messages and a list of received messages
 */
public class Chat implements Serializable {
    private final ArrayList<Message> sentMessages;
    private final ArrayList<Message> receivedMessages;

    /**
     * Class constructor
     */
    public Chat(){
        this.sentMessages = new ArrayList<>();
        this.receivedMessages = new ArrayList<>();
    }

    /**
     * Class constructor with attributes, used in case of server disconnection
     * @param sentMessages - the list of sent messages from the player
     * @param receivedMessages - the list of received messages to the player
     */
    public Chat(ArrayList<Message> sentMessages, ArrayList<Message> receivedMessages) {
        this.sentMessages = sentMessages;
        this.receivedMessages = receivedMessages;
    }

    /**
     * getter
     * @return receivedMessages - the list of received messages
     */
    public ArrayList<Message> getReceivedMessages() {
        return receivedMessages;
    }
    /**
     * getter
     * @return sentMessages - the list of sent messages
     */
    public ArrayList<Message> getSentMessages() {
        return sentMessages;
    }

    /**
     * getter
     * @param sender - the player that sent the messages
     * @return list of the messages received from a specified player
     */
    public ArrayList<Message> getReceivedMessagesFromPlayer(Player sender) {
        int i;
        ArrayList<Message> list= new ArrayList<>();
        for(i=0;i<receivedMessages.size();i++){
            if(receivedMessages.get(i).getSender()==sender && !receivedMessages.get(i).isGlobal()){
                list.add(receivedMessages.get(i));
            }
        }
        return list;
    }

    /**
     * getter
     * @param receiver - the player that received the messages
     * @return list of the messages sent to a specified player
     */
    public ArrayList<Message> getSentMessagesToPlayer(Player receiver) {
        int i;
        ArrayList<Message> list= new ArrayList<>();
        for(i=0;i<sentMessages.size();i++){
            if(sentMessages.get(i).getReceiver()==receiver&& !sentMessages.get(i).isGlobal()){
                list.add(sentMessages.get(i));
            }
        }
        return list;
    }

    /**
     * getter
     * @return list of global received message
     */
    public ArrayList<Message> getGlobalReceivedMessages() {
        int i;
        ArrayList<Message> list= new ArrayList<>();
        for(i=0;i<receivedMessages.size();i++){
            if(receivedMessages.get(i).isGlobal()){
                list.add(receivedMessages.get(i));
            }
        }
        return list;
    }

    /**
     * getter
     * @return list of global sent messages
     */
    public ArrayList<Message> getGlobalSentMessages() {
        int i;
        ArrayList<Message> list= new ArrayList<>();
        for(i=0;i<sentMessages.size();i++){
            if(sentMessages.get(i).isGlobal()){
                list.add(sentMessages.get(i));
            }
        }
        return list;
    }

    /**
     * getter
     * @return a list of the sent and received messages in the global chat in the order they have been sent
     */
    public ArrayList<Message> getGlobalChat(){
        ArrayList<Message> sent=this.getGlobalSentMessages();
        ArrayList<Message> received=this.getGlobalReceivedMessages();
        ArrayList<Message> chat=new ArrayList<>();
        int i=0;
        int j=0;
        while(i<sent.size()||j<received.size()){
            if(i>=sent.size()){
                while(j<received.size()){
                    chat.add(received.get(j));
                    j++;
                }
                return chat;
            }
            if(j>=received.size()){
                while(i<sent.size()){
                    chat.add(sent.get(i));
                    i++;
                }
                return chat;
            }
            if(sent.get(i).getOrder()<=received.get(j).getOrder()){
                chat.add(sent.get(i));
                i++;
            } else {
                chat.add(received.get(j));
                j++;
            }
        }
        return chat;
    }

    /**
     * getter
     * @param player - the player from which you want to get the private chat
     * @return list of the sent and received messages in a private chat in the order they have been sent
     */
    public ArrayList<Message> getPrivateChat(Player player){
        ArrayList<Message> sent=this.getSentMessagesToPlayer(player);
        ArrayList<Message> received=this.getReceivedMessagesFromPlayer(player);
        ArrayList<Message> chat=new ArrayList<>();
        int i=0;
        int j=0;
        while(i<sent.size()||j<received.size()){
            if(i>=sent.size()){
                while(j<received.size()){
                    chat.add(received.get(j));
                    j++;
                }
                return chat;
            }
            if(j>=received.size()){
                while(i<sent.size()){
                    chat.add(sent.get(i));
                    i++;
                }
                return chat;
            }
            if(sent.get(i).getOrder()<=received.get(j).getOrder()){
                chat.add(sent.get(i));
                i++;
            } else if(sent.get(i).getOrder()>received.get(j).getOrder()) {
                chat.add(received.get(j));
                j++;
            }

        }
        return chat;
    }
}
