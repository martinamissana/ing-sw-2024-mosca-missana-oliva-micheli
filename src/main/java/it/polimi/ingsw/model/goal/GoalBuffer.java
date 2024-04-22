package it.polimi.ingsw.model.goal;
import it.polimi.ingsw.model.game.GoalsPreset;

import java.io.IOException;
import java.util.*;

/**
 * Class GoalBuffer
 * contains the goal deck (static) and two goals randomly drawn from it:
 * when the GoalBuffer is instantiated for the class Game they represent the common goals,
 * when the GoalBuffer is instantiated for the class Player they represent the choices of private goal for the player
 */
public class GoalBuffer {
    private static final ArrayList<Goal> goalDeck =new ArrayList<>();

    static {
        try {
            goalDeck.addAll(GoalsPreset.getDiagonalGoals());
            goalDeck.addAll(GoalsPreset.getLShapeGoals());
            goalDeck.addAll(GoalsPreset.getResourceGoals());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final Goal goal1;
    private final Goal goal2;

    /**
     * constructor, draws two random goals from goalDeck
     */
    public GoalBuffer() {
        goal1=getgoal();
        goal2=getgoal();
    }

    /**
     * private method to draw a random goal from the goalDeck
     * @return Goal
     */
    private Goal getgoal(){
        if(!goalDeck.isEmpty()){
        Collections.shuffle(goalDeck);
        Goal goal=goalDeck.get(0);
        goalDeck.remove(0);
        return goal;}
        else{
            return null;
        }
    }

    /**
     * getter
     * @return Goal - one of the two random goals
     */
    public Goal getGoal1() {
        return goal1;
    }

    /**
     * getter
     * @return Goal - one of the two random goals
     */
    public Goal getGoal2() {
        return goal2;
    }
}
