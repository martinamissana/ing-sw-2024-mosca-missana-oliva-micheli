package it.polimi.ingsw.model.goal;

import it.polimi.ingsw.model.game.GoalsPreset;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class GoalContainer implements Serializable {
    private final ArrayList<Goal> goals = new ArrayList<>();

    /**
     * Class constructor
     * @throws IOException thrown if I/O operations are interrupted or failed
     */
    public GoalContainer() throws IOException {
        goals.addAll(GoalsPreset.getDiagonalGoals());
        goals.addAll(GoalsPreset.getLShapeGoals());
        goals.addAll(GoalsPreset.getResourceGoals());
    }

    /**
     * Randomly extracts a goal from the list
     * @return Goal
     */
    public Goal getGoal() {
        if (goals.isEmpty()) return null;
        Collections.shuffle(goals);
        Goal goal = goals.getFirst();
        goals.removeFirst();
        return goal;
    }
}
