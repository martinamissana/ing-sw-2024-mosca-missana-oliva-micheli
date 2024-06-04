package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.player.Player;

/**
 * LobbyCreatedEvent class
 * Extends abstract class Event
 * Used to notify the virtual view of the creation of a lobby
 */
public class LobbyCreatedEvent extends Event{
    private final Player creator;
    private final Lobby lobby;
    private final Integer ID;

    /**
     * class constructor
     * @param creator who has created the lobby
     * @param lobby lobby created
     * @param ID lobby ID
     */
    public LobbyCreatedEvent(Player creator, Lobby lobby,Integer ID) {
        this.creator = creator;
        this.lobby = lobby;
        this.ID=ID;
    }

    /**
     * getter
     * @return player who has created the lobby
     */
    public Player getCreator() {
        return creator;
    }

    /**
     * getter
     * @return lobby created
     */
    public Lobby getLobby() {
        return lobby;
    }

    /**
     * getter
     * @return lobbyID
     */
    public Integer getID() {
        return ID;
    }
}
