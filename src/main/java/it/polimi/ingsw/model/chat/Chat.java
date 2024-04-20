package it.polimi.ingsw.model.chat;
import it.polimi.ingsw.model.player.Player;

import java.io.*;
import java.util.ArrayList;

/**
 * Class Chat
 * each Player has an associated chat with a list of sent messages and a list of received messages
 */
public class Chat implements Serializable {
    private ArrayList<Message> sentMessages;
    private ArrayList<Message> receivedMessages;


    /**
     * Class constructor
     */
    public Chat(){
        this.sentMessages = new ArrayList<Message>();
        this.receivedMessages = new ArrayList<Message>();
    }

    /**
     * Class constructor with attributes, used in case of server disconnection
     * @param sentMessages
     * @param receivedMessages
     */
    public Chat(ArrayList<Message> sentMessages, ArrayList<Message> receivedMessages) {
        this.sentMessages = sentMessages;
        this.receivedMessages = receivedMessages;
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
        for(i=0;i<receivedMessages.size();i++){
            if(receivedMessages.get(i).getSender()==sender && !receivedMessages.get(i).isGlobal()){
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
        for(i=0;i<sentMessages.size();i++){
            if(sentMessages.get(i).getReceiver()==receiver&& !sentMessages.get(i).isGlobal()){
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
        for(i=0;i<receivedMessages.size();i++){
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
        for(i=0;i<sentMessages.size();i++){
            if(sentMessages.get(i).isGlobal()){
                list.add(sentMessages.get(i));
            }
        }
        return list;
    }
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
