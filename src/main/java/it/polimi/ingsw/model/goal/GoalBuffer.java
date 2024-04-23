package it.polimi.ingsw.model.goal;
import it.polimi.ingsw.model.game.GoalsPreset;

import java.io.IOException;
import java.util.*;

/**
 * Class GoalBuffer
 * contains two goals randomly drawn from GoalContainer:
 * when the GoalBuffer is instantiated for the class Game they represent the common goals,
 * when the GoalBuffer is instantiated for the class Player they represent the choices of private goal for the player
 */
public class GoalBuffer {

    private final Goal goal1;
    private final Goal goal2;

    /**
     * Class constructor
     * @param goal1 random goal from GoalContainer
     * @param goal2 random goal from GoalContainer
     */
    public GoalBuffer(Goal goal1, Goal goal2) {
        this.goal1 = goal1;
        this.goal2 = goal2;
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
