package it.polimi.ingsw.model.goal;

import it.polimi.ingsw.model.commonItem.Kingdom;

/**
 * Class L_ShapeGoal<br>
 * Subclass of goals that require two cards (with the main color) in a vertical line
 * and another card (with the secondary color) adjacent to just one of them
 */
public class L_ShapeGoal extends Goal {
    private final L_ShapeGoalType type;
    private final Kingdom mainColor;
    private final Kingdom secondaryColor;

    /**
     * Class constructor
     * @param goalID         the goal's ID
     * @param points         points to be added to the player's score when they complete the goal
     * @param mainColor      color of the two cards in a vertical line
     * @param secondaryColor color of the third card
     * @param type           orientation of the goal
     */
    public L_ShapeGoal(int goalID, int points, Kingdom mainColor, Kingdom secondaryColor, L_ShapeGoalType type) {
        super(goalID, points);
        this.type = type;
        this.mainColor = mainColor;
        this.secondaryColor = secondaryColor;
    }

    /**
     * Gets the orientation of the goal
     * @return L_ShapeGoalType
     */
    public L_ShapeGoalType getType() { return type; }

    /**
     * Gets the color of the two cards in a vertical line
     * @return Kingdom
     */
    public Kingdom getMainColor() { return mainColor; }

    /**
     * Gets the color of the third card
     * @return Kingdom
     */
    public Kingdom getSecondaryColor() { return secondaryColor; }
}
