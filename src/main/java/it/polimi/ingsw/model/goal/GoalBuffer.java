package it.polimi.ingsw.model.goal;
import it.polimi.ingsw.model.player.Player;

import java.util.*;

public class GoalBuffer {
    private static List<Goal> goalDeck= new ArrayList<>();

    public GoalBuffer(List<Goal> goalDeck) {
        GoalBuffer.goalDeck =goalDeck;
    }

    public Goal getgoal(){
        Collections.shuffle(goalDeck);
        Goal goal=goalDeck.get(0);
        goalDeck.remove(0);
        return goal;
    }
}
