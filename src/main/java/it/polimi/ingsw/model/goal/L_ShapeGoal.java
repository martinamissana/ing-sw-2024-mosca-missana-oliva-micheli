package it.polimi.ingsw.model.goal;

import it.polimi.ingsw.model.commonItem.Kingdom;

public class L_ShapeGoal extends Goal{
    private final Kingdom mainColor;
    private final Kingdom secondaryColor;
    private final L_ShapeGoalType type;

    public L_ShapeGoal(int goalID,int points,Kingdom mainColor,Kingdom secondaryColor, L_ShapeGoalType type) {
        super(goalID,points);
        this.mainColor=mainColor;
        this.secondaryColor=secondaryColor;
        this.type=type;
    }

    public L_ShapeGoalType getType() {
        return type;
    }
    public Kingdom getMainColor() {
        return mainColor;
    }
    public Kingdom getSecondaryColor() {
        return secondaryColor;
    }

}
