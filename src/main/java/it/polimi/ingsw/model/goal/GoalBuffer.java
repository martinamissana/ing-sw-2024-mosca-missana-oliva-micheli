package it.polimi.ingsw.model.goal;
import it.polimi.ingsw.model.game.GoalsPreset;

import java.io.IOException;
import java.util.*;

/**
 * Class GoalBuffer
 * contains the goal deck and draws randomly from it
 */
public class GoalBuffer {
    private static final ArrayList<Goal> goalDeck=new ArrayList<>();

    /**
     * constructor, adds goals from json files to goalDeck
     * @throws IOException produced by failed or interrupted I/O operations
     */
    public GoalBuffer() throws IOException {
        GoalBuffer.goalDeck.addAll(GoalsPreset.getDiagonalGoals());
        GoalBuffer.goalDeck.addAll(GoalsPreset.getLShapeGoals());
        GoalBuffer.goalDeck.addAll(GoalsPreset.getResourceGoals());
    }

    /**
     * draws a random goal from the goalDeck
     * @return Goal
     */
    public Goal getgoal(){
        if(!goalDeck.isEmpty()){
        Collections.shuffle(goalDeck);
        Goal goal=goalDeck.get(0);
        goalDeck.remove(0);
        return goal;}
        else{
            return null;
        }
    }
}
