package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.player.Player;

/**
 * LobbyJoinedEvent class
 * Extends abstract class Event
 * Used to notify the virtual view when a player joins the lobby
 */
public class LobbyJoinedEvent extends Event{
    private final Player player;
    private final Integer ID;

    /**
     * class constructor
     * @param player who joined the lobby
     * @param ID lobbyID
     */
    public LobbyJoinedEvent(Player player,Integer ID) {
        this.player = player;
        this.ID=ID;
    }

    /**
     * getter
     * @return player who joined the lobby
     */
    public Player getPlayer() {
        return player;
    }
    public Integer getID() {
        return ID;
    }
}
