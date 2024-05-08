package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.chat.Message;

public class ChatMessageAddedEvent extends Event{
    private final Message m;
    private final Integer lobbyID;

    public ChatMessageAddedEvent(Message m, Integer lobbyID) {
        this.m = m;
        this.lobbyID = lobbyID;
    }

    public Message getM() {
        return m;
    }

    public Integer getLobbyID() {
        return lobbyID;
    }
}
