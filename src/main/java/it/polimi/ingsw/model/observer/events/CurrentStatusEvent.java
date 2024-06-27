package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.game.Lobby;

import java.util.HashMap;

/**
 *  CurrentStatusEvent class<br>
 *  Extends abstract class Event<br>
 *  Used to notify the virtual view of the client on the active lobbies
 */
public class CurrentStatusEvent extends Event{

    private final HashMap<Integer, Lobby> lobbies;
    private final String nickname;

    /**
     * Class constructor
     * @param lobbies active lobbies
     * @param nickname name of who requested
     */
    public CurrentStatusEvent(HashMap<Integer, Lobby> lobbies, String nickname) {
        this.lobbies = lobbies;
        this.nickname = nickname;
    }

    /**
     * @return active lobbies
     */
    public HashMap<Integer, Lobby> getLobbies() { return lobbies; }

    /**
     * @return nickname of who requested
     */
    public String getNickname() { return nickname; }
}
