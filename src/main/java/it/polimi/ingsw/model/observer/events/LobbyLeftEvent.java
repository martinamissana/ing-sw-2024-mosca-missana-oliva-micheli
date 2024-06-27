package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.player.Player;

/**
 * LobbyLeftEvent class<br>
 * Extends abstract class Event<br>
 * Used to notify the virtual view when a player lefts the lobby
 */
public class LobbyLeftEvent extends Event{
    private final Player player;
    private final Lobby lobby;
    private final Integer ID;

    /**
     * Class constructor
     * @param player who left
     * @param lobby lobby left
     * @param ID lobbyID
     */
    public LobbyLeftEvent(Player player ,Lobby lobby,Integer ID) {
        this.player = player;
        this.lobby = lobby;
        this.ID=ID;
    }

    /**
     * @return player who left
     */
    public Player getPlayer() { return player; }

    /**
     * @return lobby left
     */
    public Lobby getLobby() { return lobby; }

    /**
     * @return ID
     */
    public Integer getID() { return ID; }
}
