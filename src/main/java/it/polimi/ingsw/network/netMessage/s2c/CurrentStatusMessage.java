package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.network.netMessage.NetMessage;

import java.util.HashMap;
import java.util.Scanner;

public class CurrentStatusMessage extends NetMessage {
    private final HashMap<Integer, Lobby> lobbies;
    private final String nickname;

    public CurrentStatusMessage(HashMap<Integer, Lobby> lobbies, String nickname) {
        this.lobbies= new HashMap<>(lobbies);
        this.nickname = nickname;
    }

    public HashMap<Integer, Lobby> getLobbies() {
        return lobbies;
    }
}
