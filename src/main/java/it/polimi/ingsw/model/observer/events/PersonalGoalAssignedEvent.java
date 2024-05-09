package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.model.player.Player;

public class PersonalGoalAssignedEvent extends Event{
    private final Player player;
    private final Goal goal;

    public PersonalGoalAssignedEvent(Player player, Goal goal) {
        this.player = player;
        this.goal = goal;
    }

    public Player getPlayer() {
        return player;
    }

    public Goal getGoal() {
        return goal;
    }
}
