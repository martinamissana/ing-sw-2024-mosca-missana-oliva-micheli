package it.polimi.ingsw.model.goal;

import it.polimi.ingsw.model.commonItem.Kingdom;

/**
 * Class DiagonalGoal<br>
 * Subclass of Goal, it represents the goals that require a configuration of three cards in a diagonal line to be completed
 */
public class DiagonalGoal extends Goal {
    private final Kingdom color;
    private final DiagonalGoalType type;

    /**
     * Class constructor
     * @param goalID the goal's ID
     * @param points points to be added to the player's score when they complete the goal
     * @param color  color of the cards required
     * @param type   can be {@code UPWARD} or {@code DOWNWARD} (from {@code DiagonalGoalType})
     */
    public DiagonalGoal(int goalID, int points, Kingdom color, DiagonalGoalType type) {
        super(goalID, points);
        this.color = color;
        this.type = type;
    }

    /**
     * gets the color of the cards in the configuration, represented by the Kingdom
     * @return Kingdom
     */
    public Kingdom getColor() { return color; }

    /**
     * gets the goal's type
     * @return DiagonalGoalType
     */
    public DiagonalGoalType getType() { return type; }
}

