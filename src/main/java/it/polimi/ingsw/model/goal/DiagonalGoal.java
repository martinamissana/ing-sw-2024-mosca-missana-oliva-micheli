package it.polimi.ingsw.model.goal;
import it.polimi.ingsw.model.commonItem.Kingdom;

/**
 * Class DiagonalGoal
 * subclass of Goal, it represents the goals that require three cards in a diagonal line
 */
public class DiagonalGoal extends Goal {
    private final Kingdom color;
    private final DiagonalGoalType type;

    /**
     * Class constructor
     * @param goalID - specifies univocally the goal
     * @param points - points of the goal
     * @param color - color of the cards required
     * @param type - it can be UPWARD or DOWNWARD (DiagonalGoalType)
     */
    public DiagonalGoal(int goalID,int points, Kingdom color, DiagonalGoalType type) {
        super(goalID,points);
        this.type=type;
        this.color=color;
    }

    /** gets the color, represented by the Kingdom
     * @return Kingdom
     */
    public Kingdom getColor(){
        return color;
    }

    /**
     * gets the type (DOWNWARD or UPWARD)
     * @return DiagonalGoalType
     */
    public DiagonalGoalType getType() {
        return type;
    }

}

