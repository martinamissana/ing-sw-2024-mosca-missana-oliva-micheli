package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.game.GamePhase;
import it.polimi.ingsw.network.netMessage.NetMessage;

/**
 * Class GamePhaseChangedMessage
 * used to inform the views that the game phase has changed
 */
public class GamePhaseChangedMessage extends NetMessage {
    private final Integer ID;
    private final GamePhase gamePhase;

    /**
     * Class constructor
     * @param ID the game ID
     * @param gamePhase the current game phase
     */
    public GamePhaseChangedMessage(Integer ID, GamePhase gamePhase) {
        this.ID = ID;
        this.gamePhase = gamePhase;
    }

    /**
     * @return Integer
     */
    public Integer getID() {
        return ID;
    }

    /**
     * @return GamePhase
     */
    public GamePhase getGamePhase() {
        return gamePhase;
    }
}
