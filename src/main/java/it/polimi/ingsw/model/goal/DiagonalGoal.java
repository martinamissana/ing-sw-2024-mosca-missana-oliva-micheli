package it.polimi.ingsw.model.goal;
import it.polimi.ingsw.model.commonItem.Kingdom;

public class DiagonalGoal extends Goal {
    private final Kingdom color;
    private final DiagonalGoalType type;

    public DiagonalGoal(int goalID,int points, Kingdom color, DiagonalGoalType type) {
        super(goalID,points);
        this.type=type;
        this.color=color;
    }

    public Kingdom getColor(){
        return color;
    }
    public DiagonalGoalType getType() {
        return type;
    }

}

