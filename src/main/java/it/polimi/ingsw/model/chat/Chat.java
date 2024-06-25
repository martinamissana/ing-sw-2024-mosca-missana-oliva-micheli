package it.polimi.ingsw.model.chat;

import it.polimi.ingsw.model.player.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
    public ArrayList<Message> getSentMessages() { return sentMessages; }

    /**
     * @return the player's list of received messages
     */
    public ArrayList<Message> getReceivedMessages() { return receivedMessages; }

    /**
     * @param player any other player in the lobby
     * @return a list of all private messages exchanged by this chat's player
     * and the specified one, chronologically ordered (less to most recent)<br>
     */
    public ArrayList<Message> getPrivateChat(Player player) {
        ArrayList<Message> res = this.getMessagesSentTo(player);
        res.addAll(this.getMessagesReceivedFrom(player));
        return (ArrayList<Message>) res.stream().sorted(Comparator.comparingInt(Message::getOrder)).toList();
    }

    /**
     * @param receiver any other player in the lobby
     * @return a list of all private messages that the player sent to the specified player<br>
     * returns an empty list if {@code receiver} is the same player this Chat is associated with
     */
    private ArrayList<Message> getMessagesSentTo(Player receiver) {
        return (ArrayList<Message>) sentMessages.stream()
                .filter(m -> !m.isGlobal() && // global messages aren't included
                        m.getReceiver().equals(receiver))
                .toList();
    }

    /**
     * @param sender any other player in the lobby
     * @return a list of all private messages that the player received from the specified player<br>
     * returns an empty list if {@code sender} is the same player this Chat is associated with
     */
    private ArrayList<Message> getMessagesReceivedFrom(Player sender) {
        return (ArrayList<Message>) receivedMessages.stream()
                .filter(m -> !m.isGlobal() && // global messages aren't included
                        m.getSender().equals(sender))
                .toList();
    }

    /**
     * @return a list of all global messages, chronologically ordered (less to most recent)
     */
    public ArrayList<Message> getGlobalChat() {
        ArrayList<Message> res = this.getSentGlobalMessages();
        res.addAll(this.getReceivedGlobalMessages());
        return (ArrayList<Message>) res.stream().sorted(Comparator.comparingInt(Message::getOrder)).toList();
    }

    /**
     * @return a list of all messages sent to the global chat by the player
     */
    private ArrayList<Message> getSentGlobalMessages() {
        return (ArrayList<Message>) sentMessages.stream().filter(Message::isGlobal).toList();
    }

    /**
     * @return a list of all messages received by the player from the global
     */
    private ArrayList<Message> getReceivedGlobalMessages() {
        return (ArrayList<Message>) receivedMessages.stream().filter(Message::isGlobal).toList();
    }
}
