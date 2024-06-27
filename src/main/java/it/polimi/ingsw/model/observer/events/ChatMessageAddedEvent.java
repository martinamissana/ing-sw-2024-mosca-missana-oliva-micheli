package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.chat.Message;

/**
 *  ChatMessageAddedEvent class<br>
 *  Extends abstract class Event<br>
 *  Used to notify the virtual view of the clients when a message is added in chat
 */
public class ChatMessageAddedEvent extends Event{
    private final Message m;
    private final Integer lobbyID;

    /**
     * Class constructor
     * @param m message, it contains text, sender and receiver of the chat message added
     * @param lobbyID ID of the lobby/game
     */
    public ChatMessageAddedEvent(Message m, Integer lobbyID) {
        this.m = m;
        this.lobbyID = lobbyID;
    }

    /**
     * @return message, it contains text, sender and receiver of the chat message added
     */
    public Message getM() { return m; }

    /**
     * @return ID of the lobby/game
     */
    public Integer getLobbyID() { return lobbyID; }
}
