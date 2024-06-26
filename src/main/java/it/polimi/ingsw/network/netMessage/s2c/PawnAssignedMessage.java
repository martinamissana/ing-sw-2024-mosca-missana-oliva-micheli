package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

/**
 * Class PawnAssignedMessage
 * used to inform the views that a pawn color has been assigned to a player
 */
public class PawnAssignedMessage extends NetMessage {
    Player player;
    Pawn color;
    Integer lobbyID;

    /**
     * Class constructor
     * @param player the player
     * @param color  the color assigned
     * @param lobbyID the game ID
     */
    public PawnAssignedMessage(Player player, Pawn color,Integer lobbyID) {
        this.player = player;
        this.color = color;
        this.lobbyID=lobbyID;
    }

    /**
     * @return Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return Pawn
     */
    public Pawn getColor() {
        return color;
    }

    /**
     * @return Integer
     */
    public Integer getID() {
        return lobbyID;
    }
}
