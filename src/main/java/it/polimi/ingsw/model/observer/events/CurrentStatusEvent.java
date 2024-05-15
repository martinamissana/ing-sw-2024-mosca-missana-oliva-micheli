package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.game.Lobby;

import java.util.HashMap;

public class CurrentStatusEvent extends Event{

    private final HashMap<Integer, Lobby> lobbies;

    public CurrentStatusEvent(HashMap<Integer, Lobby> lobbies) {
        this.lobbies = lobbies;
    }

    public HashMap<Integer, Lobby> getLobbies() {
        return lobbies;
    }
}
