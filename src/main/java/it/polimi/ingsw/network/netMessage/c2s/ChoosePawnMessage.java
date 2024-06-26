package it.polimi.ingsw.network.netMessage.c2s;

import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.model.player.Player;

/**
 * Class ChoosePawnMessage
 * used to choose the color of the pawn while in lobby
 */
public class ChoosePawnMessage extends NetMessage {
    Integer lobbyID;
    Player player;
    Pawn color;

    /**
     * Class constructor
     * @param lobbyID the game ID
     * @param player the player
     * @param color the color chosen
     */
    public ChoosePawnMessage(Integer lobbyID, Player player, Pawn color) {
        this.lobbyID = lobbyID;
        this.player = player;
        this.color = color;
    }

    /**
     * @return Integer
     */
    public Integer getLobbyID() {
        return lobbyID;
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
}
