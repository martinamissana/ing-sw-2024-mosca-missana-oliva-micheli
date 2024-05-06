package it.polimi.ingsw.network.netMessage.c2s;

import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.model.player.Player;

public class ChoosePawnMessage extends NetMessage {
    Integer lobbyID;
    Player player;
    Pawn color;

    public ChoosePawnMessage(Integer lobbyID, Player player, Pawn color) {
        this.lobbyID = lobbyID;
        this.player = player;
        this.color = color;
    }

    public Integer getLobbyID() {
        return lobbyID;
    }

    public Player getPlayer() {
        return player;
    }

    public Pawn getColor() {
        return color;
    }
}
