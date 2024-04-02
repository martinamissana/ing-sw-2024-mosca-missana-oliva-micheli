package it.polimi.ingsw.model.goal;
import it.polimi.ingsw.model.game.GoalsPreset;

import java.io.IOException;
import java.util.*;

/**
 * Class GoalBuffer
 * contains the goal deck
 */
public class GoalBuffer {
    private static final ArrayList<Goal> goalDeck=new ArrayList<>();

    /**
     * constructor, add goals from json files to goalDeck
     * @throws IOException
     */
    public GoalBuffer() throws IOException {
        GoalBuffer.goalDeck.addAll(GoalsPreset.getDiagonalGoals());
        GoalBuffer.goalDeck.addAll(GoalsPreset.getLShapeGoals());
        GoalBuffer.goalDeck.addAll(GoalsPreset.getResourceGoals());
    }

    /**
     * @return random goal from the goalDeck
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
