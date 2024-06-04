package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.chat.Message;

/**
 *  ChatMessageAddedEvent class
 *  Extends abstract class Event
 *  Used to notify the virtual view of the clients when a message is added in chat
 */
public class ChatMessageAddedEvent extends Event{
    private final Message m;
    private final Integer lobbyID;

    /**
     * class constructor
     * @param m message, it contains text, sender and receiver of the chat message added
     * @param lobbyID ID of the lobby/game
     */
    public ChatMessageAddedEvent(Message m, Integer lobbyID) {
        this.m = m;
        this.lobbyID = lobbyID;
    }

    /**
     * getter
     * @return message, it contains text, sender and receiver of the chat message added
     */
    public Message getM() {
        return m;
    }

    /**
     * getter
     * @return ID of the lobby/game
     */
    public Integer getLobbyID() {
        return lobbyID;
    }
}
