package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;

public class SecretGoalsListAssignedEvent extends Event{
    private final ArrayList<Goal> list;
    private final Player player;

    public SecretGoalsListAssignedEvent(ArrayList<Goal> list, Player player) {
        this.list = list;
        this.player = player;
    }

    public ArrayList<Goal> getList() {
        return list;
    }

    public Player getPlayer() {
        return player;
    }
}
