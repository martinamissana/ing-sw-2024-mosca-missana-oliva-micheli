package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

public class PawnAssignedMessage extends NetMessage {
    Player player;
    Pawn color;

    public PawnAssignedMessage(Player player, Pawn color) {
        this.player = player;
        this.color = color;
    }

    public Player getPlayer() {
        return player;
    }

    public Pawn getColor() {
        return color;
    }
}
