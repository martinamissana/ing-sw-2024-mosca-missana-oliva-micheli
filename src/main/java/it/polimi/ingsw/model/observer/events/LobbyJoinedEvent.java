package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.player.Player;

public class LobbyJoinedEvent extends Event{
    private final Player player;
    private final Integer ID;

    public LobbyJoinedEvent(Player creator,Integer ID) {
        this.player = creator;
        this.ID=ID;
    }

    public Player getPlayer() {
        return player;
    }
    public Integer getID() {
        return ID;
    }
}
