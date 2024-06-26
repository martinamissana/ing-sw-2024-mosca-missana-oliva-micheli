package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.network.netMessage.NetMessage;

/**
 * Class ChatMessageAddedMessage
 * used to inform the views that a message has been added to a chat
 */
public class ChatMessageAddedMessage extends NetMessage {
    private final Message m;
    private final Integer lobbyID;

    /**
     * Class constructor
     * @param m the chat message
     * @param lobbyID the game ID
     */
    public ChatMessageAddedMessage(Message m, Integer lobbyID) {
        this.m = m;
        this.lobbyID = lobbyID;
    }

    /**
     * @return Message
     */
    public Message getM() {
        return m;
    }

    /**
     * @return Integer
     */
    public Integer getLobbyID() {
        return lobbyID;
    }
}
