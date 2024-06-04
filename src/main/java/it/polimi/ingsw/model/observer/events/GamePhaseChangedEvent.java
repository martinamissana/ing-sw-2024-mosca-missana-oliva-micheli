package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.game.GamePhase;

/**
 * GamePhaseChangedEvent class
 * Extends abstract class Event
 * Used to notify the virtual view of the clients that the game phase has been changed [draw - play]
 */
public class GamePhaseChangedEvent  extends Event{
    private final Integer ID;
    private final GamePhase gamePhase;

    /**
     * class constructor
     * @param ID gameID
     * @param gamePhase phase of the game
     */
    public GamePhaseChangedEvent(Integer ID, GamePhase gamePhase) {
        this.ID = ID;
        this.gamePhase = gamePhase;
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
     * @return new phase of the game
     */
    public GamePhase getGamePhase() {
        return gamePhase;
    }
}
