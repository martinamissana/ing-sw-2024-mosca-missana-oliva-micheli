package it.polimi.ingsw.model.goal;

import java.io.Serializable;

/**
 * Abstract Class Goal<br>
 * Defines all the basic attributes and methods common to all goals in Codex Naturalis
 */
public abstract class Goal implements Serializable {
    private final int GoalID;
    private final int points;

    /**
     * Class constructor
     * @param goalID the goal's ID
     * @param points points to be added to the player's score when they complete the goal
     */
    public Goal(int goalID, int points) {
        this.GoalID = goalID;
        this.points = points;
    }

    /**
     * Gets the goal's ID
     * @return int
     */
    public int getGoalID() { return GoalID; }

    /**
     * Gets the goal's points
     * @return int
     */
    public int getPoints() { return points; }
}