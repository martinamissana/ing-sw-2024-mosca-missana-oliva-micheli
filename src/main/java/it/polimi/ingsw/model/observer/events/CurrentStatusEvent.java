package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.game.Lobby;

import java.util.HashMap;

public class CurrentStatusEvent extends Event{

    private final HashMap<Integer, Lobby> lobbies;

    public String getNickname() {
        return nickname;
    }

    private final String nickname;

    public CurrentStatusEvent(HashMap<Integer, Lobby> lobbies, String nickname) {
        this.lobbies = lobbies;
        this.nickname = nickname;
    }

    public HashMap<Integer, Lobby> getLobbies() {
        return lobbies;
    }
}
