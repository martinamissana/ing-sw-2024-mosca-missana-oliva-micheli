package it.polimi.ingsw.network.netMessage.c2s;

import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.network.netMessage.NetMessage;

/**
 * Class SendMessage
 * used to request the server to send a message in a chat
 */
public class SendMessage extends NetMessage {
    private final Message m;
    private final Integer lobbyID;

    /**
     * Class constructor
     * @param m the message
     * @param lobbyID the game ID
     */
    public SendMessage(Message m, Integer lobbyID) {
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
