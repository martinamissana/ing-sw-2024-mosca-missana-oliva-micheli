package it.polimi.ingsw.model.goal;

public abstract class Goal {
    private final int GoalID;
    private final int points;

    public Goal(int goalID,int points) {
        this.GoalID = goalID;
        this.points=points;
    }

    public int getPoints(){
        return points;
    }
    public int getGoalID() {
        return GoalID;
    }
}