package it.polimi.ingsw.network.netMessage.c2s;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

public class CreateLobbyMessage extends NetMessage {
    private final Player creator;
    private final int numOfPlayers;

    public CreateLobbyMessage(int numOfPlayers, Player creator) {
        this.creator = creator;
        this.numOfPlayers=numOfPlayers;
    }

    public Player getCreator() {
        return creator;
    }
    public int getNumOfPlayers() {
        return numOfPlayers;
    }
}
