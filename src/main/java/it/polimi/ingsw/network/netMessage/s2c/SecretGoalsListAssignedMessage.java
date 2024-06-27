package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

import java.util.ArrayList;

/**
 * Class SecretGoalsListAssignedMessage
 * used to send to the view the list of choosable goals
 */
public class SecretGoalsListAssignedMessage extends NetMessage {
    private final ArrayList<Goal> list;
    private final Player player;

    /**
     * Class constructor
     * @param list  list of choosable secret goals
     * @param player the player who has to choose
     */
    public SecretGoalsListAssignedMessage(ArrayList<Goal> list, Player player) {
        this.list = new ArrayList<>(list);
        this.player = player;
    }

    /**
     * @return ArrayList of choosable goals
     */
    public ArrayList<Goal> getList() {
        return list;
    }

    /**
     * @return Player
     */
    public Player getPlayer() {
        return player;
    }
}
