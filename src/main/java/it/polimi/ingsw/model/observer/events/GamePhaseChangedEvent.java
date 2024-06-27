package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.game.GamePhase;

/**
 * GamePhaseChangedEvent class<br>
 * Extends abstract class Event<br>
 * Used to notify the virtual view of the clients that the game phase has been changed [draw - play]
 */
public class GamePhaseChangedEvent  extends Event{
    private final Integer ID;
    private final GamePhase gamePhase;

    /**
     * Class constructor
     * @param ID gameID
     * @param gamePhase phase of the game
     */
    public GamePhaseChangedEvent(Integer ID, GamePhase gamePhase) {
        this.ID = ID;
        this.gamePhase = gamePhase;
    }

    /**
     * @return gameID
     */
    public Integer getID() { return ID; }

    /**
     * @return new phase of the game
     */
    public GamePhase getGamePhase() { return gamePhase; }
}
