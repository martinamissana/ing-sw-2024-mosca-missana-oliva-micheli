package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

/**
 * Class SecretGoalAssignedMessage
 * used to inform the view that the secret goal has been assigned to the player
 */
public class SecretGoalAssignedMessage extends NetMessage {
    private final Player player;
    private final Goal goal;

    /**
     * Class constructor
     * @param player the player who chose the secret goal
     * @param goal   the goal chosen
     */
    public SecretGoalAssignedMessage(Player player, Goal goal) {
        this.player = player;
        this.goal = goal;
    }

    /**
     * @return Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return Goal
     */
    public Goal getGoal() {
        return goal;
    }
}
