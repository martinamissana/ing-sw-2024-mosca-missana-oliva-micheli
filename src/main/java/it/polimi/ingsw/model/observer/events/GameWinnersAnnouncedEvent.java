package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * GameWinnersAnnouncedEvent class
 * Extends abstract class Event
 * Used to notify the virtual view of the winners of the game
 */
public class GameWinnersAnnouncedEvent  extends Event {
    private Integer ID;
    private ArrayList<Player> winners = new ArrayList<>();
    private HashMap <Player, Integer> goalsDone = new HashMap<>();

    /**
     * class constructor
     * @param ID gameID
     * @param winners list of the winners
     */
    public GameWinnersAnnouncedEvent(Integer ID, ArrayList<Player> winners, HashMap<Player,Integer> goalsDone) {
        this.ID = ID;
        this.winners = winners;
        this.goalsDone=goalsDone;
    }

    /**
     * getter
     * @return Hashmap <Player, Integer>  how many goals every player completed
     */
    public HashMap<Player, Integer> getGoalsDone() {
        return goalsDone;
    }
    /**
     * getter
     * @return gameID
     */
    public Integer getID() {
        return ID;
    }

    /**
     * getter
     * @return list of winners
     */
    public ArrayList<Player> getWinners() {
        return winners;
    }
}
