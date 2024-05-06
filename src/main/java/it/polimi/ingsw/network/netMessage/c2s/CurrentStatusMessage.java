package it.polimi.ingsw.network.netMessage.c2s;

import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.network.netMessage.NetMessage;

import java.util.HashMap;

public class CurrentStatusMessage extends NetMessage {
    private final HashMap<Integer, Lobby> lobbies;

    public CurrentStatusMessage(HashMap<Integer, Lobby> lobbies) {
        this.lobbies = lobbies;
    }

    public HashMap<Integer, Lobby> getLobbies() {
        return lobbies;
    }
}
