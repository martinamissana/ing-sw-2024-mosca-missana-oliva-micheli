package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.player.Player;

/**
 * ScoreIncrementedEvent class
 * Extends abstract class Event
 * Used to notify the virtual view when a player score is incremented
 */
public class ScoreIncrementedEvent extends Event {
    private final Integer ID;
    private final Player player;
    private final int points; //the amount of points added to a player's score

    /**
     * class constructor
     * @param ID game ID
     * @param player who's score is incremented
     * @param points amount of point added
     */
    public ScoreIncrementedEvent(Integer ID,Player player, int points) {
        this.ID = ID;
        this.player = player;
        this.points = points;
    }

    /**
     * getter
     * @return game ID
     */
    public Integer getID() {
        return ID;
    }

    /**
     * getter
     * @return player whose score is incremented
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * getter
     * @return amount of point added
     */
    public int getPoints() {
        return points;
    }
}
