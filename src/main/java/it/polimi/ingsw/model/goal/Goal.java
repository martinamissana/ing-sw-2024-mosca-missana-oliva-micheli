package it.polimi.ingsw.model.goal;

public abstract class Goal {
    int GoalID;
    int owner;
    public int getId() {
        return GoalID;
    }
    public int getOwner(){
        return owner;
    }
    public void evaluate(){
    }
}
