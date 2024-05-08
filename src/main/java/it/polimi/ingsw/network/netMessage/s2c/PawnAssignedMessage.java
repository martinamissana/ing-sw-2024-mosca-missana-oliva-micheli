package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

public class PawnAssignedMessage extends NetMessage {
    Player player;
    Pawn color;
    Integer lobbyID;

    public PawnAssignedMessage(Player player, Pawn color,Integer lobbyID) {
        this.player = player;
        this.color = color;
        this.lobbyID=lobbyID;
    }

    public Player getPlayer() {
        return player;
    }

    public Pawn getColor() {
        return color;
    }

    public Integer getLobbyID() {
        return lobbyID;
    }
}
