package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.model.player.Player;

/**
 * ScoreIncrementedEvent class<br>
 * Extends abstract class Event<br>
 * Used to notify the virtual view when the secret goal is assigned
 */
public class SecretGoalAssignedEvent extends Event{
    private final Player player;
    private final Goal goal;

    /**
     * Class constructor
     * @param player who chose the goal
     * @param goal goal chosen
     */
    public SecretGoalAssignedEvent(Player player, Goal goal) {
        this.player = player;
        this.goal = goal;
    }

    /**
     * @return player who chose the goal
     */
    public Player getPlayer() { return player; }

    /**
     * @return goal chosen
     */
    public Goal getGoal() { return goal; }
}
