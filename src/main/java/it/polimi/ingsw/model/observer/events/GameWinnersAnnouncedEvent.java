package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;

/**
 * GameWinnersAnnouncedEvent class
 * Extends abstract class Event
 * Used to notify the virtual view of the winners of the game
 */
public class GameWinnersAnnouncedEvent  extends Event {
    private Integer ID;
    private ArrayList<Player> winners=new ArrayList<>();

    /**
     * class constructor
     * @param ID gameID
     * @param winners list of the winners
     */
    public GameWinnersAnnouncedEvent(Integer ID, ArrayList<Player> winners) {
        this.ID = ID;
        this.winners = winners;
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
