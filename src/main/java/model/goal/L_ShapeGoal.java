package model.goal;

import model.commonItem.Kingdom;

public class L_ShapeGoal extends Goal{
    private Kingdom mainColor;
    private Kingdom secondaryColor;
    private L_ShapeGoalType type;
    public L_ShapeGoalType getType() {
        return type;
    }
    public Kingdom getMainColor() {
        return mainColor;
    }
    public Kingdom getSecondaryColor() {
        return secondaryColor;
    }

    @Override
    public void evaluate() {

    }
}
