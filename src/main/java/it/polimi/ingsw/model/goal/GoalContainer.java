package it.polimi.ingsw.model.goal;

import it.polimi.ingsw.model.game.GoalsPreset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class GoalContainer {
    private final ArrayList<Goal> goals = new ArrayList<>();

    /**
     * class constructor
     * @throws IOException produced by failed or interrupted I/O operations
     */
    public GoalContainer() throws IOException {
        goals.addAll(GoalsPreset.getDiagonalGoals());
        goals.addAll(GoalsPreset.getLShapeGoals());
        goals.addAll(GoalsPreset.getResourceGoals());
    }

    /**
     * method to pick a random goal from goalContainer
     * @return Goal
     */
    public Goal getGoal() {
        if(!goals.isEmpty()) {
            Collections.shuffle(goals);
            Goal goal = goals.getFirst();
            goals.removeFirst();
            return goal;
        }
        else {
            return null;
        }
    }
}
