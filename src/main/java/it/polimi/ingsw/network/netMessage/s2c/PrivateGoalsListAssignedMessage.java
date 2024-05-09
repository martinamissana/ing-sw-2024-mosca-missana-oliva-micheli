package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

import java.util.ArrayList;

public class PrivateGoalsListAssignedMessage extends NetMessage {
    private final ArrayList<Goal> list;
    private final Player player;

    public PrivateGoalsListAssignedMessage(ArrayList<Goal> list, Player player) {
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
