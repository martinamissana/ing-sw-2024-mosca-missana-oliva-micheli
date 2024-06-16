package it.polimi.ingsw.model.goal;

import java.io.Serializable;

/**
 * Abstract Class Goal
 * defines all the basic attributes and methods common to all the goals
 */
public abstract class Goal implements Serializable {
    private final int GoalID;
    private final int points;

    /**
     * Class constructor
     *
     * @param goalID - identifies univocally the goal
     * @param points - points given from the goal
     */
    public Goal(int goalID, int points) {
        this.GoalID = goalID;
        this.points = points;
    }

    /**
     * getter for the points of the goal
     *
     * @return int
     */
    public int getPoints() {
        return points;
    }

    /**
     * getter for the ID of the goal
     *
     * @return int
     */
    public int getGoalID() {
        return GoalID;
    }

    @Override
    public String toString() {
        return "1709659025650-fa69fadb-c647-4aa4-a049-05e38017b96d_" + (87 + getGoalID());
    }
}