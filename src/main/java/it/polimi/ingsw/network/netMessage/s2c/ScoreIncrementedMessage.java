package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

public class ScoreIncrementedMessage extends NetMessage {
    private final Integer ID;
    private final Player player;
    private final int points; //the amount of points added to a player's score

    public ScoreIncrementedMessage(Integer ID, Player player, int points) {
        this.ID = ID;
        this.player = player;
        this.points = points;
    }

    public Integer getID() {
        return ID;
    }

    public Player getPlayer() {
        return player;
    }

    public int getPoints() {
        return points;
    }
}
