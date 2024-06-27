package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.player.Player;

/**
 * LobbyJoinedEvent class<br>
 * Extends abstract class Event<br>
 * Used to notify the virtual view when a player joins the lobby
 */
public class LobbyJoinedEvent extends Event{
    private final Player player;
    private final Integer ID;

    /**
     * Class constructor
     * @param player who joined the lobby
     * @param ID lobbyID
     */
    public LobbyJoinedEvent(Player player,Integer ID) {
        this.player = player;
        this.ID=ID;
    }

    /**
     * @return player who joined the lobby
     */
    public Player getPlayer() { return player; }

    /**
     * @return ID
     */
    public Integer getID() { return ID; }
}
