package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;

/**
 * ScoreIncrementedEvent class<br>
 * Extends abstract class Event<br>
 * Used to notify the virtual view when the secret goal list to choose the secret goal from is assigned
 */
public class SecretGoalsListAssignedEvent extends Event{
    private final ArrayList<Goal> list;
    private final Player player;

    /**
     * Class constructor
     * @param list list of goals to choose the secret goal from
     * @param player player who has to choose
     */
    public SecretGoalsListAssignedEvent(ArrayList<Goal> list, Player player) {
        this.list = list;
        this.player = player;
    }

    /**
     * @return list of goals to choose the secret goal from
     */
    public ArrayList<Goal> getList() { return list; }

    /**
     * @return player who has to choose
     */
    public Player getPlayer() { return player; }
}
