package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

import java.util.ArrayList;

public class SecretGoalsListAssignedMessage extends NetMessage {
    private final ArrayList<Goal> list = new ArrayList<>();
    private final Player player;

    public SecretGoalsListAssignedMessage(ArrayList<Goal> list, Player player) {
        this.list.addAll(list);
        this.player = player;
    }

    public ArrayList<Goal> getList() {
        return list;
    }

    public Player getPlayer() {
        return player;
    }
}
