package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;

/**
 * LoginEvent class
 * Extends abstract class Event
 * Used to notify the virtual view when a player choose a pawn
 */
public class PawnAssignedEvent extends Event{
    Player player;
    Pawn color;
    Integer lobbyID;

    /**
     * class constructor
     * @param player player who chose the pawn
     * @param color color chosen
     * @param lobbyID ID of the lobby
     */
    public PawnAssignedEvent(Player player, Pawn color,Integer lobbyID) {
        this.player = player;
        this.color = color;
        this.lobbyID=lobbyID;
    }

    /**
     * getter
     * @return player who chose the pawn
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * getter
     * @return the color chosen
     */
    public Pawn getColor() {
        return color;
    }

    /**
     * getter
     * @return lobbyID
     */
    public Integer getLobbyID() {
        return lobbyID;
    }
}
