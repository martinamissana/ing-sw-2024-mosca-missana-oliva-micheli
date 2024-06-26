package it.polimi.ingsw.model.chat;

import it.polimi.ingsw.model.player.Player;

import java.io.*;
import java.util.ArrayList;

/**
 * Class Chat<br>
 * each Player has an associated Chat, which contains separate lists for sent and received messages
 */
public class Chat implements Serializable {
    private final ArrayList<Message> sentMessages;
    private final ArrayList<Message> receivedMessages;

    /**
     * Class constructor
     */
    public Chat() {
        this.sentMessages = new ArrayList<>();
        this.receivedMessages = new ArrayList<>();
    }

    /**
     * @return the player's list of sent messages
     */
    public ArrayList<Message> getReceivedMessages() {
        return receivedMessages;
    }

    /**
     * @return the player's list of received messages
     */
    public ArrayList<Message> getSentMessages() {
        return sentMessages;
    }

    /**
     * @param sender any other player in the lobby
     * @return a list of all private messages that the player received from the specified player<br>
     * returns an empty list if {@code sender} is the same player this Chat is associated with
     */
    private ArrayList<Message> getReceivedMessagesFromPlayer(Player sender) {
        int i;
        ArrayList<Message> list = new ArrayList<>();
        for (i = 0; i < receivedMessages.size(); i++) {
            if (!receivedMessages.get(i).isGlobal() && receivedMessages.get(i).getSender().equals(sender)) {
                list.add(receivedMessages.get(i));
            }
        }
        return list;
    }

    /**
     * @param receiver any other player in the lobby
     * @return a list of all private messages that the player sent to the specified player<br>
     * returns an empty list if {@code receiver} is the same player this Chat is associated with
     */
    private ArrayList<Message> getSentMessagesToPlayer(Player receiver) {
        int i;
        ArrayList<Message> list = new ArrayList<>();
        for (i = 0; i < sentMessages.size(); i++) {
            if (!sentMessages.get(i).isGlobal() && sentMessages.get(i).getReceiver().equals(receiver)) {
                list.add(sentMessages.get(i));
            }
        }
        return list;
    }

    /**
     * @return a list of all messages received by the player from the global
     */
    private ArrayList<Message> getGlobalReceivedMessages() {
        int i;
        ArrayList<Message> list = new ArrayList<>();
        for (i = 0; i < receivedMessages.size(); i++) {
            if (receivedMessages.get(i).isGlobal()) {
                list.add(receivedMessages.get(i));
            }
        }
        return list;
    }

    /**
     * @return a list of all messages sent to the global chat by the player
     */
    private ArrayList<Message> getGlobalSentMessages() {
        int i;
        ArrayList<Message> list = new ArrayList<>();
        for (i = 0; i < sentMessages.size(); i++) {
            if (sentMessages.get(i).isGlobal()) {
                list.add(sentMessages.get(i));
            }
        }
        return list;
    }

    /**
     * @return a list of all global messages, chronologically ordered (less to most recent)
     */
    public ArrayList<Message> getGlobalChat() {
        ArrayList<Message> sent = this.getGlobalSentMessages();
        ArrayList<Message> received = this.getGlobalReceivedMessages();
        ArrayList<Message> chat = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (i < sent.size() || j < received.size()) {
            if (i >= sent.size()) {
                while (j < received.size()) {
                    chat.add(received.get(j));
                    j++;
                }
                return chat;
            }
            if (j >= received.size()) {
                while (i < sent.size()) {
                    chat.add(sent.get(i));
                    i++;
                }
                return chat;
            }
            if (sent.get(i).getOrder() <= received.get(j).getOrder()) {
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
     * @param player any other player in the lobby
     * @return a list of all private messages exchanged by this chat's player
     * and the specified one, chronologically ordered (less to most recent)<br>
     */
    public ArrayList<Message> getPrivateChat(Player player) {
        ArrayList<Message> sent = this.getSentMessagesToPlayer(player);
        ArrayList<Message> received = this.getReceivedMessagesFromPlayer(player);
        ArrayList<Message> chat = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (i < sent.size() || j < received.size()) {
            if (i >= sent.size()) {
                while (j < received.size()) {
                    chat.add(received.get(j));
                    j++;
                }
                return chat;
            }
            if (j >= received.size()) {
                while (i < sent.size()) {
                    chat.add(sent.get(i));
                    i++;
                }
                return chat;
            }
            if (sent.get(i).getOrder() <= received.get(j).getOrder()) {
                chat.add(sent.get(i));
                i++;
            } else if (sent.get(i).getOrder() > received.get(j).getOrder()) {
                chat.add(received.get(j));
                j++;
            }

        }
        return chat;
    }
}
