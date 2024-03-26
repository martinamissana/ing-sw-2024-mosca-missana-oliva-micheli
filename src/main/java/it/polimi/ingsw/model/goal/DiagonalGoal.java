package it.polimi.ingsw.model.goal;

import it.polimi.ingsw.model.commonItem.Kingdom;

public class DiagonalGoal extends Goal {
    private Kingdom color;
    private DiagonalGoalType type;
    public Kingdom getColor(){
    return color;
    }
    public DiagonalGoalType getType() {
        return type;
    }

    @Override
    public void evaluate() {
    }
}
