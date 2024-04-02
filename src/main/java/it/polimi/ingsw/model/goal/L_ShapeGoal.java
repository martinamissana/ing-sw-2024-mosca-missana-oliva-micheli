package it.polimi.ingsw.model.goal;

import it.polimi.ingsw.model.commonItem.Kingdom;

/**
 * Class L_ShapeGoal
 * subclass of goal for the goals with two cards (with the maincolor) in a vertical line and another card (with the secondarycolor) next to them
 */
public class L_ShapeGoal extends Goal{
    private final Kingdom mainColor;
    private final Kingdom secondaryColor;
    private final L_ShapeGoalType type;

    /**
     * Class constructor
     * @param goalID - identifies univocally the goal
     * @param points - points given from the goal
     * @param mainColor - color of 2 cards in vertical line
     * @param secondaryColor - color of the third card
     * @param type - orientation of the goal
     */
    public L_ShapeGoal(int goalID,int points,Kingdom mainColor,Kingdom secondaryColor, L_ShapeGoalType type) {
        super(goalID,points);
        this.mainColor=mainColor;
        this.secondaryColor=secondaryColor;
        this.type=type;
    }

    /**
     * gets the orientation of the goal
     * @return L_ShapeGoalType
     */
    public L_ShapeGoalType getType() {
        return type;
    }

    /**
     * gets the color of 2 cards in vertical line
     * @return Kingdom
     */
    public Kingdom getMainColor() {
        return mainColor;
    }

    /**
     * gets the color of the third card
     * @return Kingdom
     */
    public Kingdom getSecondaryColor() {
        return secondaryColor;
    }

}
