package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;

public class PawnAssignedEvent extends Event{
    Player player;
    Pawn color;
    Integer lobbyID;

    public PawnAssignedEvent(Player player, Pawn color,Integer lobbyID) {
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
