package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class GameWinnersAnnouncedMessage
 * used to send to the views the list of winners of a game
 */
public class GameWinnersAnnouncedMessage extends NetMessage {
    private Integer ID;
    private ArrayList<Player> winners;
    private HashMap <Player,Integer> goalsDone;

    /**
     * Class constructor
     * @param ID the game ID
     * @param winners list of the winners
     * @param goalsDone amount of goals completed for each player
     */
    public GameWinnersAnnouncedMessage(Integer ID, ArrayList<Player> winners, HashMap<Player,Integer> goalsDone) {
        this.ID = ID;
        this.winners=new ArrayList<>(winners);
        this.goalsDone=goalsDone;
    }

    /**
     * @return HashMap<Player,Integer>
     */
    public HashMap<Player, Integer> getGoalsDone() {
        return goalsDone;
    }

    /**
     * @return Integer
     */
    public Integer getID() {
        return ID;
    }

    /**
     * @return ArrayList<Player>
     */
    public ArrayList<Player> getWinners() {
        return winners;
    }
}
