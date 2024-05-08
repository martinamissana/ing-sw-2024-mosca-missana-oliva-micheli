package it.polimi.ingsw.network.netMessage.c2s;

import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.network.netMessage.NetMessage;

public class SendMessage extends NetMessage {
    private final Message m;
    private final Integer lobbyID;

    public SendMessage(Message m, Integer lobbyID) {
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
