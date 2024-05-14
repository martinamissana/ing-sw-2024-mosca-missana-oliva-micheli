package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.network.netMessage.NetMessage;

import java.util.HashMap;

public class CurrentStatusMessage extends NetMessage {
    private final HashMap<Integer, Lobby> lobbies= new HashMap<>();

    public CurrentStatusMessage(HashMap<Integer, Lobby> lobbies) {
        
        this.lobbies.putAll(lobbies);
    }

    public HashMap<Integer, Lobby> getLobbies() {
        return lobbies;
    }
}
