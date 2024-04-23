package it.polimi.ingsw.model.goal;

import it.polimi.ingsw.model.game.GoalsPreset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class GoalContainer {

    private final ArrayList<Goal> goalContainer =new ArrayList<>();

    /**
     * class constructor
     * @throws IOException produced by failed or interrupted I/O operations
     */
    public GoalContainer() throws IOException {
        goalContainer.addAll(GoalsPreset.getDiagonalGoals());
        goalContainer.addAll(GoalsPreset.getLShapeGoals());
        goalContainer.addAll(GoalsPreset.getResourceGoals());
    }

    /**
     * method to pick a random goal from goalContainer
     * @return Goal
     */
    public Goal getgoal(){
        if(!goalContainer.isEmpty()){
            Collections.shuffle(goalContainer);
            Goal goal=goalContainer.getFirst();
            goalContainer.removeFirst();
            return goal;}
        else{
            return null;
        }
    }
}
