package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.player.Player;

public class ScoreIncrementedEvent extends Event {
    private final Integer ID;
    private final Player player;
    private final int points; //the amount of points added to a player's score

    public ScoreIncrementedEvent(Integer ID,Player player, int points) {
        this.ID = ID;
        this.player = player;
        this.points = points;
    }

    public Integer getID() {
        return ID;
    }

    public Player getPlayer() {
        return player;
    }

    public int getPoints() {
        return points;
    }
}
