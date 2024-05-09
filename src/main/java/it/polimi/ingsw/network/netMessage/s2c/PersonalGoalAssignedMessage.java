package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

public class PersonalGoalAssignedMessage extends NetMessage {
    private final Player player;
    private final Goal goal;

    public PersonalGoalAssignedMessage(Player player, Goal goal) {
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
