package it.polimi.ingsw.model.goal;

import it.polimi.ingsw.model.player.Player;

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
    public int getId() {
        return GoalID;
    }
    public int evaluate(Player player){
        return 0;
    }
}