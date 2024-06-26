package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

/**
 * Class ScoreIncrementedMessage
 * used to inform the views that a player's score has changed
 */
public class ScoreIncrementedMessage extends NetMessage {
    private final Integer ID;
    private final Player player;
    private final int points;

    /**
     * Class constructor
     * @param ID        the gameID
     * @param player    the player
     * @param points    the amount of points added to a player's score
     */
    public ScoreIncrementedMessage(Integer ID, Player player, int points) {
        this.ID = ID;
        this.player = player;
        this.points = points;
    }

    /**
     * @return Integer
     */
    public Integer getID() {
        return ID;
    }

    /**
     * @return Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return int
     */
    public int getPoints() {
        return points;
    }
}
