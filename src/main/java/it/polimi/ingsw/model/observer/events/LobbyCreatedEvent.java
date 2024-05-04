package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.player.Player;

public class LobbyCreatedEvent extends Event{
    private final Player creator;
    private final Lobby lobby;
    private final Integer ID;

    public LobbyCreatedEvent(Player creator, Lobby lobby,Integer ID) {
        this.creator = creator;
        this.lobby = lobby;
        this.ID=ID;
    }

    public Player getCreator() {
        return creator;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public Integer getID() {
        return ID;
    }
}
